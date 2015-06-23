package ase.analysis.analysis;

import ase.analysis.Constants;
import ase.shared.model.analysis.TripAnalysisResult;
import ase.shared.model.analysis.TripsAnalysis;
import ase.analysis.analysis.prioritizedMessaging.MessagePriority;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedJmsTemplate;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedMessage;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedMessageCreator;
import ase.shared.commands.CommandFactory;
import ase.shared.dto.AnalysisRequestDTO;
import ase.shared.dto.AnalysisResponseDTO;
import ase.shared.model.analysis.Report;
import ase.shared.model.analysis.ReportTimeSpan;
import ase.shared.model.simulation.Line;
import ase.shared.model.simulation.RealtimeData;
import ase.shared.model.simulation.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Michael on 21.06.2015.
 */
@Component
public class AnalysisService {

    @Autowired
    private PrioritizedJmsTemplate prioritizedJmsTemplate;

    @Autowired
    private CommandFactory commandFactory;

    public AnalysisResponseDTO queueForAnalysis(AnalysisRequestDTO analysisRequestDTO, MessagePriority messagePriority) {

        System.out.println("Analysis started...");

        AnalysisResponseDTO analysisResponseDTO = new AnalysisResponseDTO();
        analysisResponseDTO.setOk(true);

        //todo: validate request

        // queue for analysis if request is valid
        if(analysisResponseDTO.isOk()) {

            try {
                prioritizedJmsTemplate.send(new PrioritizedMessageCreator(new PrioritizedMessage<Object>(analysisRequestDTO, messagePriority)));
            } catch (JmsException e) {
                e.printStackTrace();
                analysisResponseDTO.setOk(false);
                analysisResponseDTO.setMessage("Queueing for analysis failed! Please try again");
            }
        }

        System.out.println("Analysis finished...");
        return analysisResponseDTO;
    }


    @JmsListener(destination = Constants.ANALYSIS_QUEUE_NAME)
    public void jmsAnalyseTarget(AnalysisRequestDTO analysisRequestDTO) {
        System.out.println("Analysis request received (JMS)");
        List<Station> stations = Arrays.asList(commandFactory.getStationsBetweenCommand(analysisRequestDTO.getLine(), analysisRequestDTO.getStationFrom(), analysisRequestDTO.getStationTo()).getResult());

        if (stations.size() <= 1) {
            //useless analysis (bad request)
            System.out.println("Bad request");
            return;
        }

        List<Station> directions = Arrays.asList(commandFactory.getDirectionsCommand(analysisRequestDTO.getLine()).getResult());

        //sort by position asc
        directions.sort((Station stationA, Station stationB) -> Integer.compare(stationA.getPosition(), stationB.getPosition()));

        Station directionLow = directions.get(0);
        Station directionHigh = directions.get(1);

        Station entryStation = stations.stream().filter(x -> x.getName().equals(analysisRequestDTO.getStationFrom())).findFirst().get();
        Station exitStation = stations.stream().filter(x -> x.getName().equals(analysisRequestDTO.getStationTo())).findFirst().get();

        Station direction = entryStation.getPosition() < exitStation.getPosition() ? directionHigh : directionLow;

        TripsAnalysis tripsAnalysis = analyzeRT(entryStation, exitStation, analysisRequestDTO.getLine(), direction.getName(), analysisRequestDTO.getFrom(), analysisRequestDTO.getTo());
        tripsAnalysis.analyze();

        // start analyzing

        Report report = new Report();
        report.setLines(Arrays.asList(new Line(analysisRequestDTO.getLine())));
        report.setTime(new ReportTimeSpan(analysisRequestDTO.getFrom(), analysisRequestDTO.getTo()));
        report.setStations(stations);

        report.setTripsAnalysis(tripsAnalysis);

        ResponseEntity<Object> execute = commandFactory.createReportCommand(report).execute();


        analysisRequestDTO.getReportMetadata().setCreatedAt(new Date());
        commandFactory.createReportMetadataCommand(analysisRequestDTO.getReportMetadata()).execute();

        System.out.println("Analysis finished (jms)");
    }

    private TripsAnalysis analyzeRT(Station entryStation, Station exitStation, String line, String direction, Date from, Date to) {
        List<RealtimeData> entryStationRT = commandFactory.getRealtimeDataByStationAndTWCommand(line, direction, entryStation.getNumber(), from, to).toList();
        List<RealtimeData> exitStationRT = commandFactory.getRealtimeDataByStationAndTWCommand(line, direction, exitStation.getNumber(), from, to).toList();

        //all trips started at the entry station in the time frame
        List<Integer> entryStationTripNumbers = entryStationRT.stream().map(x -> x.getTrain().getTripNumber()).distinct().collect(Collectors.toList());

        // remove trips form the exit station which have not started at the entry station in the supplied time frame
        List<RealtimeData> arrivingTripsRT = exitStationRT.stream().filter(x -> entryStationTripNumbers.contains(x.getTrain().getTripNumber())).collect(Collectors.toList());

        //trips ending at the exit station in the time frame, after first filtering
        List<Integer> filteredExitStationTripNumbers = arrivingTripsRT.stream().map(x -> x.getTrain().getTripNumber()).distinct().collect(Collectors.toList());

        // remove trips form the entry station which have not ended at the exit station in the supplied time frame
        List<RealtimeData> startingTripsRT = entryStationRT.stream().filter(x -> filteredExitStationTripNumbers.contains(x.getTrain().getTripNumber())).collect(Collectors.toList());

        //no we have two lists of filtered trip data containing only the trips, both starting and arriving within the supplied time frame

        Map<Integer, List<RealtimeData>> entryArrivingTripRT = startingTripsRT.stream()/*.filter(x -> arrivingTripsRT.contains(x.getTrain().getTripNumber()))*/.collect(Collectors.groupingBy(x -> x.getTrain().getTripNumber()));
        Map<Integer, List<RealtimeData>> exitArrivingTripRT = arrivingTripsRT.stream()/*.filter(x -> startingTripsRT.contains(x.getTrain().getTripNumber()))*/.collect(Collectors.groupingBy(x -> x.getTrain().getTripNumber()));

        return joinRT(entryArrivingTripRT, exitArrivingTripRT);
    }

    /**
     * join the realtime data
     * both maps contain the same trip ids!!!
     * @param entryArrivingTripRT
     * @param exitArrivingTripRT
     */
    private TripsAnalysis joinRT(Map<Integer, List<RealtimeData>> entryArrivingTripRT, Map<Integer, List<RealtimeData>> exitArrivingTripRT) {

        TripsAnalysis tripsAnalysis = new TripsAnalysis();

        for (Integer tripNumber : entryArrivingTripRT.keySet()) {

            TripAnalysisResult tripAnalysisResult = new TripAnalysisResult(tripNumber);
            tripsAnalysis.addTrip(tripAnalysisResult);

            List<RealtimeData> entryRT = entryArrivingTripRT.get(tripNumber);
            List<RealtimeData> exitRT = exitArrivingTripRT.get(tripNumber);

            // sort by currenttime, required for further processing
            entryRT.sort((o1, o2) -> o1.getCurrentTime().compareTo(o2.getCurrentTime()));
            exitRT.sort((o1, o2) -> o1.getCurrentTime().compareTo(o2.getCurrentTime()));

            tripAnalysisResult.setEntryRT(entryRT);
            tripAnalysisResult.setExitRT(exitRT);
        }

        return tripsAnalysis;
    }

//    private void followTrains(List<Map<Integer, List<RealtimeData>>> differentTrainsEntry, String line, String direction, Date from, Date to, List<Station> stations, Station entryStation, Station exitStation) {
//
//        //all the trains arriving at the station in time
//        List<Integer> trainIds = differentTrainsEntry.stream().flatMap(x -> x.keySet().stream()).distinct().collect(Collectors.toList());
//
//        // the numbers of the stations on the way to destination
//        List<Integer> stationNumbers = stations.stream().map(x->x.getNumber()).collect(Collectors.toList());
//
//        // for every train check if it arrives in time at the destination
//        for (Integer trainId : trainIds) {
//            Collection<RealtimeData> trainRT = commandFactory.getRealtimeDataByTrainAndTWCommand(line, direction, trainId, from, to).execute();
//
//            List<RealtimeData> sortedFilteredTrainRT = trainRT.stream().filter(x -> stationNumbers.contains(x.getStation().getNumber())).sorted((o1, o2) -> o1.getCurrentTime().compareTo(o2.getCurrentTime())).collect(Collectors.toList());
//
//            //Map<Date, List<RealtimeData>> collect = sortedFilteredTrainRT.stream().collect(Collectors.groupingBy((RealtimeData realtimeData) -> realtimeData.getCurrentTime()));
//
//            Map<Integer, List<RealtimeData>> collect = sortedFilteredTrainRT.stream().collect(Collectors.groupingBy((RealtimeData realtimeData) -> realtimeData.getStation().getNumber()));
//
//            //sort inner lists, depends on direction!
//            int idxChange;
//            if(entryStation.getPosition() < exitStation.getPosition()) {
//                collect.forEach((integer, realtimeDatas) -> realtimeDatas.sort((o1, o2) -> o1.getCurrentTime().compareTo(o2.getCurrentTime())));
//                idxChange = 1;
//            }
//            else {
//                collect.forEach((integer, realtimeDatas) -> realtimeDatas.sort((o1, o2) -> o2.getCurrentTime().compareTo(o1.getCurrentTime())));
//                idxChange = -1;
//            }
//
//
//
//            //copy inner lists to prevent unwanted changes anywhere else
//            //filter
//            for (Integer currentIdx : collect.keySet()) {
//                collect.put(currentIdx, collect.get(currentIdx).stream().filter(x->x.isTrainInStation()).collect(Collectors.toList()));
//            }
//
//
//            int idx = entryStation.getPosition();
//            while (idx != exitStation.getPosition()) {
//
//
//
//
//
//                idx+=idxChange;     //++ or --
//            }
//
//
//
//            //List<Map<Integer, List<RealtimeData>>> steps = splitDifferentTrains(sortedFilteredTrainRT);
//
//            //Map<RealtimeData, RealtimeData> suitableTrains = extractSuitableTrains(collect, stations.get(0), stations.get(stations.size() - 1));
//        }
//    }
//
//    private Map<RealtimeData, RealtimeData> extractSuitableTrains(Map<Date, List<RealtimeData>> steps, Station start, Station end) {
//
//        Map<RealtimeData, RealtimeData> result = new HashMap<>();
//
//
//
//
//        return result;
//    }
//
//    private List<Map<Integer, List<RealtimeData>>> followTrains(String line, String direction, Date from, Date to, List<Map<Integer, List<RealtimeData>>> differentTrainsEntry, List<Station> stations) {
//
//        List<Map<Integer, List<RealtimeData>>> currentDifferentTrains = differentTrainsEntry;
//        // stations are sorted!
//        // skip entry station
//
//        int minutesSkipped = 0;
//        for (Station station : stations.stream().skip(1).collect(Collectors.toList())) {
//
//            for (Map<Integer, List<RealtimeData>> trains : currentDifferentTrains) {
//                Date reducedStartDate = new DateTime(from).plusMinutes(minutesSkipped).toDate();
//
//                List<RealtimeData> currentStationRT = commandFactory.getRealtimeDataByStationAndTWCommand(line, direction, station.getNumber(), reducedStartDate, to).toList();
//                currentDifferentTrains = splitDifferentTrains(currentStationRT);
//
//                RealtimeData currentMinimumRT = filterDifferentTrains(differentTrainsEntry, currentDifferentTrains);
//                if (currentMinimumRT == null)
//                    minutesSkipped = Minutes.minutesBetween(new DateTime(from), new DateTime(currentMinimumRT.getCurrentTime())).getMinutes();
//            }
//        }
//
//
//        return null;
//    }
//
//    /**
//     * removes all the trains from entry which have never reached the current station
//     * removes all the trains from current station which have never started in entry
//     *
//     * removes trains from current which have been a different ride than
//     * @param differentTrainsEntry
//     * @param currentDifferentTrains
//     * @return
//     */
//    private RealtimeData filterDifferentTrains(List<Map<Integer, List<RealtimeData>>> differentTrainsEntry, List<Map<Integer, List<RealtimeData>>> currentDifferentTrains) {
//
//        //reduce trains
//
//
//        // removes all the trains from differentTrainsEntry which never reach currentDifferentTrains
//        getIntersection(differentTrainsEntry, currentDifferentTrains);
//        // removes all the trains from currentDifferentTrains which never started at entry
//        getIntersection(currentDifferentTrains, differentTrainsEntry);
//
//
//        Stream<RealtimeData> realtimeDataStream = currentDifferentTrains.stream().flatMap(x -> x.values().stream().flatMap(i -> i.stream().map(rt -> rt)));
//        Optional<RealtimeData> minimumTime = realtimeDataStream.min((o1, o2) -> o1.getCurrentTime().compareTo(o2.getCurrentTime()));
//        if (minimumTime.isPresent())
//            return minimumTime.get();
//        return null;
//    }
//
//    /**
//     * removes all the trains from modified which never reach lookup
//     * @param modified
//     * @param lookup
//     */
//    private void getIntersection(List<Map<Integer, List<RealtimeData>>> modified, List<Map<Integer, List<RealtimeData>>> lookup) {
//        for (Map<Integer,List<RealtimeData>> entryItem: new LinkedList<>(modified)) {
//            int key = entryItem.keySet().iterator().next();     // they are all the same, therefore use the first which has to exist anyway
//
//            if(!lookup.stream().anyMatch(x -> x.keySet().contains(key))) {
//                modified.remove(entryItem);
//            }
//        }
//    }
//
//
//    /**
//     * splits all the RT data into different train arrivals at the station
//     * @param realtimeData
//     * @return
//     */
//    private List<Map<Integer, List<RealtimeData>>> splitDifferentTrains(List<RealtimeData> realtimeData) {
//
//        Map<Integer, List<RealtimeData>> groupedByTrain = realtimeData.stream().collect(Collectors.groupingBy(x -> x.getTrain().getNumber()));
//
//        List<Map<Integer, List<RealtimeData>>> result = new LinkedList<>();
//
//        for (Integer trainNumber : groupedByTrain.keySet()) {
//
//            List<RealtimeData> trainData = groupedByTrain.get(trainNumber);
//            trainData.sort((o1, o2) -> o1.getCurrentTime().compareTo(o2.getCurrentTime()));
//
//            // skip data till first regular train arrives
//            // split into trains
//            List<RealtimeData> inStation = null;
//            Iterator<RealtimeData> iterator = trainData.iterator();
//
//            do {
//                Map<Integer, List<RealtimeData>> currentMap = new HashMap<>();
//                RealtimeData currentRealtimeData = null;
//                inStation = new LinkedList<>();
//
//                // skip not in station ones
//                while (iterator.hasNext()) {
//                    currentRealtimeData = iterator.next();
//                    if (currentRealtimeData.isTrainInStation()){
//                        break;
//                    }
//                }
//
//                if(currentRealtimeData != null && currentRealtimeData.isTrainInStation()) {
//                    inStation.add(currentRealtimeData);
//                }
//
//                //take while in station
//                while(iterator.hasNext()) {
//                    currentRealtimeData = iterator.next();
//                    if (currentRealtimeData.isTrainInStation()) {
//                        inStation.add(currentRealtimeData);
//                    } else {
//                        break;
//                    }
//                }
//
//                if(inStation.size() > 0) {
//                    currentMap.put(trainNumber, inStation);
//                    result.add(currentMap);
//                }
//
//            }while (inStation.size() > 0);
//        }
//
//        return result;
//    }

}
