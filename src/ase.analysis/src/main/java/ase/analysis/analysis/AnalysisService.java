package ase.analysis.analysis;

import ase.analysis.Constants;
import ase.analysis.analysis.model.DayAnalysis;
import ase.analysis.analysis.model.TripAnalysis;
import ase.analysis.analysis.model.TripsAnalysis;
import ase.analysis.analysis.prioritizedMessaging.MessagePriority;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedJmsTemplate;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedMessage;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedMessageCreator;
import ase.shared.commands.CommandFactory;
import ase.shared.commands.CreateResult;
import ase.shared.dto.AnalysisRequestDTO;
import ase.shared.dto.AnalysisResponseDTO;
import ase.shared.model.analysis.Report;
import ase.shared.model.analysis.ReportTimeSpan;
import ase.shared.model.notification.Notification;
import ase.shared.model.simulation.Line;
import ase.shared.model.simulation.RealtimeData;
import ase.shared.model.simulation.Station;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

    public AnalysisResponseDTO error(String message) {
        AnalysisResponseDTO responseDTO = new AnalysisResponseDTO();
        responseDTO.setOk(false);
        responseDTO.setMessage(message);
        return responseDTO;
    }

    public AnalysisResponseDTO queueForAnalysis(AnalysisRequestDTO analysisRequestDTO, MessagePriority messagePriority) {

        System.out.println("Basic validation of analysis request for user " + analysisRequestDTO.getUserId());

        AnalysisResponseDTO analysisResponseDTO = new AnalysisResponseDTO();

        // TODO: there should be some additonal validation to make sure only valid requests are processed but, lets skip this for this mini project
        if(StringUtils.isEmpty(analysisRequestDTO.getUserId()))
            return error("userId required");

        if(StringUtils.isEmpty(analysisRequestDTO.getStationFrom()))
            return error("stationFrom required");

        if(StringUtils.isEmpty(analysisRequestDTO.getStationTo()))
            return error("stationTo required");

        if(StringUtils.isEmpty(analysisRequestDTO.getLine()))
            return error("line required");

        if(analysisRequestDTO.getFrom().after(analysisRequestDTO.getTo()))
            return error("Invalid values for from, to");


        System.out.println("Queuing analysis for user " + analysisRequestDTO.getUserId());
        analysisResponseDTO.setOk(true);
        analysisResponseDTO.setMessage("Queued for analysis.");

        // queue for analysis if request is valid
        if(analysisResponseDTO.isOk()) {

            try {
                prioritizedJmsTemplate.send(new PrioritizedMessageCreator(new PrioritizedMessage<Object>(analysisRequestDTO, messagePriority)));
            } catch (JmsException e) {
                e.printStackTrace();
                return error("Queueing for analysis failed! Please try again later!");
            }
        }

        System.out.println("Analysis request queued...");
        return analysisResponseDTO;
    }


    @JmsListener(destination = Constants.ANALYSIS_QUEUE_NAME)
    public void jmsAnalyseTarget(AnalysisRequestDTO analysisRequestDTO) {

        System.out.println("Processing analysis request from user " + analysisRequestDTO.getUserId() + " for line " + analysisRequestDTO.getLine() + " from " + analysisRequestDTO.getStationFrom() + " to " + analysisRequestDTO.getStationTo());
        List<Station> stations = null;
        try {
            stations = Arrays.asList(commandFactory.getStationsBetweenCommand(analysisRequestDTO.getLine(), analysisRequestDTO.getStationFrom(), analysisRequestDTO.getStationTo()).getResult());
        } catch (Exception ex) {

        }

        if (stations == null || stations.size() <= 1) {
            //useless analysis (bad request)
            System.out.println("Bad request, stop analysis for user: " + analysisRequestDTO.getUserId());
            sendNotification(analysisRequestDTO.getUserId(), "Analysis failed", "The request was invalid, stations not found!");
            return;
        }

        // get required input data for analysis (stations, directions, ...)
        try {
            List<Station> directions = Arrays.asList(commandFactory.getDirectionsCommand(analysisRequestDTO.getLine()).getResult());
            directions.sort((Station stationA, Station stationB) -> Integer.compare(stationA.getPosition(), stationB.getPosition()));
            Station directionLow = directions.get(0);
            Station directionHigh = directions.get(1);

            Station entryStation = stations.stream().filter(x -> x.getName().equals(analysisRequestDTO.getStationFrom())).findFirst().get();
            Station exitStation = stations.stream().filter(x -> x.getName().equals(analysisRequestDTO.getStationTo())).findFirst().get();

            Station direction = entryStation.getPosition() < exitStation.getPosition() ? directionHigh : directionLow;

            // perform actual analysis
            TripsAnalysis tripsAnalysis = analyse(analysisRequestDTO, entryStation, exitStation, direction);

            // create report
            Report report = createReport(analysisRequestDTO, stations, tripsAnalysis);

            // save report
            CreateResult createResult = commandFactory.createReportCommand(report).getResult();

            //save report metadata, required for evaluation
            if (createResult.isOk()) {
                analysisRequestDTO.getReportMetadata().setCreatedAt(new Date());
                analysisRequestDTO.getReportMetadata().setReportId(createResult.getLocation().substring(createResult.getLocation().lastIndexOf('/') + 1));
                commandFactory.createReportMetadataCommand(analysisRequestDTO.getReportMetadata()).execute();

                sendNotification(analysisRequestDTO.getUserId(), "Report finished", "The report for line " + analysisRequestDTO.getLine() + " from " + analysisRequestDTO.getStationFrom() + " to " + analysisRequestDTO.getStationTo() + " has been finished!\n\n" +
                        "ReportID: " + analysisRequestDTO.getReportMetadata().getReportId() + "\n\nThanks for using our service!");
                // we don't care about the notification result :)
            } else {
                System.out.println("Report failed, we don't care about such situations in this implementation. Should not happen anyway :)");
            }
        } catch (Exception ex) {
            sendNotification(analysisRequestDTO.getUserId(), "Analysis failed!", "The analysis was not possible, the request seams to be invalid!");
            ex.printStackTrace();
        }
        System.out.println("Processing finished for user " + analysisRequestDTO.getUserId());
    }

    private boolean sendNotification(String user, String subject, String message) {
        Notification notification = new Notification();
        notification.setEmail(user);
        notification.setSubject(subject);
        notification.setDate(new Date());
        notification.setMessage(message);

        CreateResult result = commandFactory.createNotificationCommand(notification).getResult();
        return result.isOk();
    }

    private TripsAnalysis analyse(AnalysisRequestDTO analysisRequestDTO, Station entryStation, Station exitStation, Station direction) {
        List<TripAnalysis> tripAnalysisList = new LinkedList<>();

        // prepare date
        DateTime currentDayStart = new DateTime(analysisRequestDTO.getFrom());
        currentDayStart = new DateTime(currentDayStart.getYear(),currentDayStart.getMonthOfYear(),currentDayStart.getDayOfMonth(), analysisRequestDTO.getHourStart(), analysisRequestDTO.getMinuteStart());
        DateTime currentDayEnd = new DateTime(currentDayStart.getYear(),currentDayStart.getMonthOfYear(), currentDayStart.getDayOfMonth(), analysisRequestDTO.getHourEnd(), analysisRequestDTO.getMinuteEnd());

        int days = Days.daysBetween(new DateTime(analysisRequestDTO.getFrom()), new DateTime(analysisRequestDTO.getTo())).getDays();

        TripsAnalysis tripsAnalysis = new TripsAnalysis();

        for (int currentDay = 0; currentDay < days; currentDay++) {
            tripAnalysisList = analyzeDayRT(entryStation, exitStation, analysisRequestDTO.getLine(), direction.getName(), currentDayStart.toDate(), currentDayEnd.toDate());

            currentDayStart = currentDayStart.plusDays(1);
            currentDayEnd = currentDayEnd.plusDays(1);
            if(tripAnalysisList.size() > 0) {
                tripsAnalysis.addDay(new DayAnalysis(tripAnalysisList));
            }
        }

        // perform actual analysis

        tripsAnalysis.analyze();
        return tripsAnalysis;
    }

    /**
     * creates the actual report from the analysis results
     * @param analysisRequestDTO
     * @param stations
     * @param tripsAnalysis
     * @return
     */
    private Report createReport(AnalysisRequestDTO analysisRequestDTO, List<Station> stations, TripsAnalysis tripsAnalysis) {
        Report report = new Report();
        report.setLines(Arrays.asList(new Line(analysisRequestDTO.getLine())));

        ReportTimeSpan reportTimeSpan = new ReportTimeSpan(analysisRequestDTO.getFrom(), analysisRequestDTO.getTo());
        reportTimeSpan.setHourFrom(analysisRequestDTO.getHourStart());
        reportTimeSpan.setHourTo(analysisRequestDTO.getHourEnd());
        reportTimeSpan.setMinuteFrom(analysisRequestDTO.getMinuteEnd());
        reportTimeSpan.setMinuteTo(analysisRequestDTO.getMinuteEnd());

        report.setTime(reportTimeSpan);
        report.setStations(stations);

        report.setTripsAnalysisResult(tripsAnalysis);
        return report;
    }

    /**
     * parses the realtime data into an analysis object
     * @param entryStation
     * @param exitStation
     * @param line
     * @param direction
     * @param from
     * @param to
     * @return
     */
    private List<TripAnalysis> analyzeDayRT(Station entryStation, Station exitStation, String line, String direction, Date from, Date to) {

        //call webservice for RT data
        List<RealtimeData> entryStationRT = commandFactory.getRealtimeDataByStationAndTWCommand(line, direction, entryStation.getNumber(), from, to).toList();
        List<RealtimeData> exitStationRT = commandFactory.getRealtimeDataByStationAndTWCommand(line, direction, exitStation.getNumber(), from, to).toList();

        // keep only rt for trains which start in entry and reach arrival station within time frame
        List<Integer> entryTrainNumbers = entryStationRT.stream().map(x->x.getTrain().getNumber()).distinct().collect(Collectors.toList());
        exitStationRT = exitStationRT.stream().filter(x->entryTrainNumbers.contains(x.getTrain().getNumber())).collect(Collectors.toList());
        List<Integer> exitTrainNumbers = exitStationRT.stream().map(x->x.getTrain().getNumber()).distinct().collect(Collectors.toList());
        entryStationRT = entryStationRT.stream().filter(x->exitTrainNumbers.contains(x.getTrain().getNumber())).collect(Collectors.toList());

        // group by train number (trip numbers are not unique!)
        Map<Integer, List<RealtimeData>> entryTrainsByTrainNumber = entryStationRT.stream().collect(Collectors.groupingBy(x -> x.getTrain().getNumber()));
        Map<Integer, List<RealtimeData>> exitTrainsByTrainNumber = exitStationRT.stream().collect(Collectors.groupingBy(x -> x.getTrain().getNumber()));


        //TripsAnalysis tripsAnalysis = new TripsAnalysis();
        List<TripAnalysis> tripAnalysisList = new LinkedList<>();

        for(Integer trainNumber : entryTrainsByTrainNumber.keySet()) {

            // limited RT to the current train, contains still all the trips from this train
            List<RealtimeData> currentTrainEntryStationRT = entryTrainsByTrainNumber.get(trainNumber);
            List<RealtimeData> currentTrainExitStationRT = exitTrainsByTrainNumber.get(trainNumber);

            //all trips started at the entry station in the time frame
            List<Integer> entryStationTripNumbers = currentTrainEntryStationRT.stream().map(x -> x.getTrain().getTripNumber()).distinct().collect(Collectors.toList());

            // remove trips form the exit station which have not started at the entry station in the supplied time frame
            List<RealtimeData> arrivingTripsRT = currentTrainExitStationRT.stream().filter(x -> entryStationTripNumbers.contains(x.getTrain().getTripNumber())).collect(Collectors.toList());

            //trips ending at the exit station in the time frame, after first filtering
            List<Integer> filteredExitStationTripNumbers = arrivingTripsRT.stream().map(x -> x.getTrain().getTripNumber()).distinct().collect(Collectors.toList());

            // remove trips form the entry station which have not ended at the exit station in the supplied time frame
            List<RealtimeData> startingTripsRT = currentTrainEntryStationRT.stream().filter(x -> filteredExitStationTripNumbers.contains(x.getTrain().getTripNumber())).collect(Collectors.toList());

            //now we have two lists of filtered trip data containing only the trips, both starting and arriving within the supplied time frame
            Map<Integer, List<RealtimeData>> entryArrivingTripRT = startingTripsRT.stream().collect(Collectors.groupingBy(x -> x.getTrain().getTripNumber()));
            Map<Integer, List<RealtimeData>> exitArrivingTripRT = arrivingTripsRT.stream().collect(Collectors.groupingBy(x -> x.getTrain().getTripNumber()));

            tripAnalysisList.addAll(joinRT(entryArrivingTripRT, exitArrivingTripRT, trainNumber));
        }
        return tripAnalysisList;
    }

    /**
     * join the realtime data
     * both maps contain the same trip ids!!!
     * @param entryArrivingTripRT
     * @param exitArrivingTripRT
     */
    private List<TripAnalysis> joinRT(Map<Integer, List<RealtimeData>> entryArrivingTripRT, Map<Integer, List<RealtimeData>> exitArrivingTripRT, int trainNumber) {
        List<TripAnalysis> result = new LinkedList<>();

        for (Integer tripNumber : entryArrivingTripRT.keySet()) {

            TripAnalysis tripAnalysisResult = new TripAnalysis(tripNumber, trainNumber);
            result.add(tripAnalysisResult);

            List<RealtimeData> entryRT = entryArrivingTripRT.get(tripNumber);
            List<RealtimeData> exitRT = exitArrivingTripRT.get(tripNumber);

            // sort by currenttime, required for further processing
            entryRT.sort((o1, o2) -> o1.getCurrentTime().compareTo(o2.getCurrentTime()));
            exitRT.sort((o1, o2) -> o1.getCurrentTime().compareTo(o2.getCurrentTime()));

            tripAnalysisResult.setEntryRT(entryRT);
            tripAnalysisResult.setExitRT(exitRT);
        }

        return result;
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
