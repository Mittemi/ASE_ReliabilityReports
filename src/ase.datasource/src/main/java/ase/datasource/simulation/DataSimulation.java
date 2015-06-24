package ase.datasource.simulation;

import ase.datasource.model.StoredRealtimeData;
import ase.shared.model.simulation.Line;
import ase.shared.model.simulation.Station;
import ase.shared.model.simulation.Train;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Michael on 20.06.2015.
 */
public class DataSimulation {

    private final boolean ENABLE_DEBUG = true;

    // begin static data

    private Random random = new Random(1);      //fixed to keep things predictable!

    private DateTime currentTime = new DateTime(2015,6,1,8,0,0);

    public DateTime getCurrentTime() {
        return currentTime;
    }

    private void println(String text) {
        if(ENABLE_DEBUG)
            System.out.println(currentTime.toString("dd.MM HH:mm") + ": " + text);
    }

    private final ConcurrentHashMap<Line, Integer> timeToTravel;

    public int getTimeToTravelPerStation(String line) {
        return getTimeToTravelPerStation(lines.get(line));
    }

    public int getTimeToTravelPerStation(Line line) {
        return timeToTravel.get(line);
    }

    public Station getFirstStation(Line line) {
        int firstStation = lineToStations.get(line).keySet().stream().map(Station::getNumber).min(Integer::compareTo).get();

        return lineToStations.get(line).inverse().get(firstStation);
    }

    public Station getLastStation(Line line) {
        int firstStation = lineToStations.get(line).keySet().stream().map(Station::getNumber).max(Integer::compareTo).get();

        return lineToStations.get(line).inverse().get(firstStation);
    }

    /**
     * Name -> Station
     */
    private final ConcurrentHashMap<String, Station> stations;

    /**
     * Name -> Line
     */
    private final ConcurrentHashMap<String, Line> lines;

    private final ConcurrentHashMap<Station, Line> stationToLine;

    /**
     * Number -> Train
     */
    private final ConcurrentHashMap<Integer, Train> trains;

    /**
     * Line -> StationsList
     */
    private final ConcurrentHashMap<Line, BiMap<Station, Integer>> lineToStations;

    private final ConcurrentHashMap<Line, ConcurrentHashMap<Integer, Train>> trainsPerLine;

    // end static data

    private final ConcurrentHashMap<Station, ConcurrentHashMap<Station, DateTime>> lastTrain;

    private final ConcurrentHashMap<Station,ConcurrentHashMap<Station, DateTime>> estimatedArrival;
    private final ConcurrentHashMap<Station,ConcurrentHashMap<Station, DateTime>> plannedArrival;
    private final ConcurrentHashMap<Station, ConcurrentHashMap<Station, Boolean>> trainInStation;
    private final ConcurrentHashMap<Train, Integer> additonalWaitTime;

    /**
     * Train -> delay
     */
    //private final ConcurrentHashMap<Train, Integer> delayTrain;

    /**
     * Train -> amount of time to wait till next move
     * =0: just arrived at current station
     * >0: on the way to the station
     * <0: ready to leave station
     */
    private final ConcurrentHashMap<Train, Integer> waitingTime;

    /**
     * Line -> error
     */
    private final ConcurrentHashMap<Line, Boolean> lineError;

    /**
     * Train -> Station (Position)
     */
    private final ConcurrentHashMap<Train, Station> position;

    /**
     * Train -> Station (Direction)
     */
    private final ConcurrentHashMap<Train, Station> direction;

    public DataSimulation() {
        stations = new ConcurrentHashMap<>();
        lines = new ConcurrentHashMap<>();
        trains = new ConcurrentHashMap<>();
        trainsPerLine = new ConcurrentHashMap<>();

        lineToStations = new ConcurrentHashMap<>();
        stationToLine = new ConcurrentHashMap<>();
        lineError = new ConcurrentHashMap<>();
       // delayTrain = new ConcurrentHashMap<>();
        waitingTime = new ConcurrentHashMap<>();
        direction = new ConcurrentHashMap<>();
        position = new ConcurrentHashMap<>();
        lastTrain = new ConcurrentHashMap<>();
        estimatedArrival = new ConcurrentHashMap<>();
        plannedArrival = new ConcurrentHashMap<>();
        trainInStation = new ConcurrentHashMap<>();
        additonalWaitTime = new ConcurrentHashMap<>();
        timeToTravel = new ConcurrentHashMap<>();

        initU1();
        initU2();
        initU3();
        checkConstraints();
    }

    private void initU3() {
        Line u3 = createLine("U3", 3);

        Station ottakring = createStation(u3, "Ottakring", 25, 1);
        Station schweglerstraße = createStation(u3, "Schweglerstraße", 26, 2);
        Station westbahnhof = createStation(u3, "Westbahnhof", 27, 3);
        Station zieglergasse = createStation(u3, "Zieglergasse", 28, 4);
        Station neubaugasse = createStation(u3, "Neubaugasse", 29, 5);
        Station volkstheater = createStation(u3, "Volkstheater", 30, 6);
        Station herrengasse = createStation(u3, "Herrengasse", 31, 7);
        Station stubentor = createStation(u3, "Stubentor", 32, 8);
        Station landstraße = createStation(u3, "Landstraße", 33, 9);
        Station rochusgasse = createStation(u3, "Rochusgasse", 34, 10);
        Station kardinal = createStation(u3, "Kardinal-Nagl-Platz", 35, 11);
        Station simmering = createStation(u3, "Simmering", 36, 12);

        initHelperData(u3);

        // Leopoldau -> Raeumannplatz
        createTrain(u3, 1, simmering, ottakring, 0);
        createTrain(u3, 2, rochusgasse, ottakring, 0);
        createTrain(u3, 3, stubentor, ottakring, 0);
        createTrain(u3, 4, volkstheater, ottakring, 0);
        createTrain(u3, 5, zieglergasse, ottakring, 0);
        createTrain(u3, 6, schweglerstraße, ottakring, 0);

        // Raeumannplatz -> Leopoldau
        createTrain(u3, 7, ottakring, simmering, 0);
        createTrain(u3, 8, westbahnhof, simmering, 0);
        createTrain(u3, 9, neubaugasse, simmering, 0);
        createTrain(u3, 10, herrengasse, simmering, 0);
        createTrain(u3, 11, landstraße, simmering, 0);
        createTrain(u3, 12, kardinal, simmering, 0);

        updateArrivalTimes(u3);
    }

    private void initU2() {
        Line u2 = createLine("U2", 4);

        Station karlsplatz = createStation(u2, "Karlsplatz", 13, 1);
        Station museumsquartier = createStation(u2, "Museumsquartier", 14, 2);
        Station volkstheater = createStation(u2, "Volkstheater", 15, 3);
        Station rathaus = createStation(u2, "Rathaus", 16, 4);
        Station schottentor = createStation(u2, "Schottentor - Universität", 17, 5);
        Station schottenring = createStation(u2, "Schottenring", 18, 6);
        Station taborstraße = createStation(u2, "Taborstraße", 19, 7);
        Station praterstern = createStation(u2, "Praterstern", 20, 8);
        Station messe = createStation(u2, "Messe", 21, 9);
        Station krieau = createStation(u2, "Krieau", 22, 10);
        Station stadion = createStation(u2, "Stadion", 23, 11);
        Station aspernstraße = createStation(u2, "Aspernstraße", 24, 12);

        initHelperData(u2);

        // Leopoldau -> Raeumannplatz
        createTrain(u2, 1, aspernstraße, karlsplatz, 0);
        createTrain(u2, 2, krieau, karlsplatz, 0);
        createTrain(u2, 3, praterstern, karlsplatz, 0);
        createTrain(u2, 4, schottenring, karlsplatz, 0);
        createTrain(u2, 5, rathaus, karlsplatz, 0);
        createTrain(u2, 6, museumsquartier, karlsplatz, 0);

        // Raeumannplatz -> Leopoldau
        createTrain(u2, 7, karlsplatz, aspernstraße, 0);
        createTrain(u2, 8, volkstheater, aspernstraße, 0);
        createTrain(u2, 9, schottentor, aspernstraße, 0);
        createTrain(u2, 10, taborstraße, aspernstraße, 0);
        createTrain(u2, 11, messe, aspernstraße, 0);
        createTrain(u2, 12, stadion, aspernstraße, 0);

        updateArrivalTimes(u2);
    }

    private void checkConstraints() {

        for (Line line : getLines()) {
            int stations = getAllStations(line).size() * getDirections(line).size();
            int trains = trainsPerLine.get(line).size();
            if(stations % trains != 0)
                throw new RuntimeException("Configuration exception: (stations * directions) % trains needs to be 0");
        }
    }

    /**
     * performs all required processing for the supplied train
     * @param trainNumber
     */
    public void tick(Integer trainNumber) {
        Train train = this.trains.get(trainNumber);

        Station currentStation = this.position.get(train);
        Station direction = this.direction.get(train);

     //   int delay = this.delayTrain.get(train);
        int waitTime = this.waitingTime.get(train);

        Line line = stationToLine.get(currentStation);

        // arrived at station, nothing to do
        if(waitTime == 1) {
            println("Train " + trainNumber + " arrived at " + currentStation.getName());
            setTrainInStation(currentStation, direction, true);    //arrived
            setWaitTime(train, 0);
        }else if(waitTime == 0) {
            Station nextStation = getNextStation(currentStation, direction);

            boolean left;
            // check if we need to turn on the current station (change direction, happens immediately )
            if(nextStation == null) {
                Station newDirection = getOtherDirection(direction);
                // train stays at the same station but changes direction
                left = moveTrainIfPossible(train, currentStation, newDirection, currentStation);
                if(left)
                    train.setTripNumber(train.getTripNumber() + 1);
            } else {
                left = moveTrainIfPossible(train, currentStation, direction, nextStation);
            }

            if(left) {
                // train will left station --> set new planned arrival date
                int factor = (getAllStations(line).size() * getDirections(line).size()) / trainsPerLine.get(line).size();

                int timeToTravelPerStation = getTimeToTravelPerStation(line);
                DateTime plannedArrival = getPlannedArrival(currentStation, direction);
                int timeBetweenTrains = timeToTravelPerStation * factor;
                DateTime newPlannedArrivalTime = plannedArrival.plusMinutes(timeBetweenTrains);

                while(Minutes.minutesBetween(newPlannedArrivalTime, currentTime).getMinutes() > timeBetweenTrains) {
                    System.out.println("INFO: Skipping the next train as planned time would drift too far from estimated");
                    newPlannedArrivalTime = newPlannedArrivalTime.plusMinutes(timeBetweenTrains);
                }
                //if(Minutes.minutesBetween(newPlannedArrivalTime, currentTime) > timeToTravelPerStation)

                setPlannedArrival(currentStation, direction, newPlannedArrivalTime);
            }

        }else {
            setWaitTime(train, waitTime - 1);
        }
    }

    private boolean moveTrainIfPossible(Train train, Station currentStation, Station direction, Station nextStation) {
        //wait when too early
        setAdditionalWaitTime(train, 0);

        DateTime plannedArrival = getPlannedArrival(currentStation, direction);
        Minutes diff = Minutes.minutesBetween(currentTime, plannedArrival);
        if(currentTime.isBefore(plannedArrival)){
            int additionalWaitingTimeInStation = diff.getMinutes();
            println("Train " + train.getNumber() + " arrived too soon, needs to wait additional " + additionalWaitingTimeInStation + " Minutes!");
            setAdditionalWaitTime(train, additionalWaitingTimeInStation);
            // not needed, statistics knows anyway !?
            // setLastTrain(currentStation,direction,currentTime);
            return false;
        }
        //end wait

        //4/3 because we generate data in the interval 30-60 seconds which should result in average of 45 seconds
        int timeToTravelPerStation = getTimeToTravelPerStation(stationToLine.get(currentStation)) * 4/3;
        int minTime = timeToTravelPerStation/2;
        int travelTime = random.nextInt(timeToTravelPerStation - minTime) + minTime;

        if(isFree(nextStation, direction)) {

            setTrainInStation(currentStation, this.direction.get(train), false);    //left
            setLastTrain(nextStation,direction,currentTime);
            setWaitTime(train, travelTime);

            this.position.put(train, nextStation);
            this.direction.put(train, direction);

            println("Train " + train.getNumber() + " left " + currentStation.getName());

            return true;

        } else {
            println("Train " + train.getNumber() + " can't leave station " + currentStation.getName() + "! Station: " + nextStation.getName() + " not free in direction " + direction.getName());
            return false;
        }
    }

    private void setWaitTime(Train train, int waitingTime) {
        this.waitingTime.put(train, waitingTime);
    }

    /**
     *
     * @param direction
     * @return other direction, unpredictable result if not called with an end station
     */
    private Station getOtherDirection(Station direction) {

        Line line = this.stationToLine.get(direction);
        BiMap<Station, Integer> stations = this.lineToStations.get(line);
        Station station;

        Station first = getFirstStation(line);
        if(direction == first) {
            station = getLastStation(line);
        }
        else {
            station = first;
        }

        assert station != null;
        assert station != direction;
        return station;
    }

    /**
     * gets all the trains on the line in the supplied direction
     * @param direction
     * @return
     */
    private List<Train> getTrainsInDirection(Station direction) {
        return trainsPerLine.get(stationToLine.get(direction)).values().stream().filter(x->this.direction.get(x) == direction).collect(Collectors.toList());
    }

    /**
     * check if the station is free in the supplied direction
     *
     * @param station
     * @param direction
     * @return
     */
    private boolean isFree(Station station, Station direction) {

        boolean hasTrain = getTrainInStation(station, direction);
        if(hasTrain) {
            return false;
        }

        Train trainThere = null;
        for (Train train : getTrainsInDirection(direction)) {
            if (this.position.get(train) == station) {
                trainThere = train;
                break;
            }
        }

        if (trainThere != null) {
            return getAdditionalWaitTime(trainThere) == 0;
        }

        return true;
    }

    /**
     *
     * @param currentStation
     * @param direction
     * @return the the next station in the supplied direction, null if we need to turn direction in the current station in order to continue
     */
    private Station getNextStation(Station currentStation, Station direction) {

        if(direction == currentStation) return null;      // we need to turn first

        Line line = this.stationToLine.get(currentStation);

        BiMap<Station, Integer> stations = this.lineToStations.get(line);

        Integer position = stations.get(currentStation);

        // -->
        if(direction == getLastStation(line)) {
            return stations.inverse().get(position + 1);
        } else  {   // <--
            return stations.inverse().get(position - 1);
        }
    }

    public List<StoredRealtimeData> move(String lineName) {

        int seconds = random.nextInt(30) + 30;

        //currentTime = currentTime.plusMinutes(1);
        currentTime = currentTime.plusSeconds(seconds);

        Line line = lines.get(lineName);
        ConcurrentHashMap<Integer, Train> trains = this.trainsPerLine.get(line);

        List<StoredRealtimeData> result = getRealtimeData(line);

        for (Train train : trains.values()) {
            tick(train.getNumber());
        }

        updateArrivalTimes(line);

        return result;
    }


    private List<StoredRealtimeData> getRealtimeData(Line line) {
        List<StoredRealtimeData> result = new LinkedList<>();
        List<Station> directions = getDirections(line);

        for(Station station : getAllStations(line)) {

            for(Station direction : directions) {

                StoredRealtimeData realtimeData = new StoredRealtimeData();

                realtimeData.setLine(line);
                realtimeData.setDirection(direction);
                realtimeData.setError(false);
                realtimeData.setStation(station);
                realtimeData.setCurrentTime(currentTime.toDate());

                Train nearestTrain = getNearestTrain(station, direction, true);
                realtimeData.setTrain(nearestTrain);

                realtimeData.setTrainInStation(getTrainInStation(station, direction));

                realtimeData.setPlannedArrival(getPlannedArrival(station, direction).toDate());

                // not in the station
                if(!realtimeData.isTrainInStation()) {
                    realtimeData.setEstimatedArrival(getEstimatedArrival(station, direction).toDate());
                }

/*
                if(getLastTrain(station, direction) != null) {
                    realtimeData.setPlannedArrival(getLastTrain(station, direction).plusMinutes(getTimeToTravelPerStation(line)).toDate());
                }else  {
                    realtimeData.setPlannedArrival(realtimeData.getEstimatedArrival());     // for the first trains --> planned = estimated
                }*/

                result.add(realtimeData);
            }
        }
        return result;
    }

    private Train getNearestTrain(Station station, Station direction) {
        return getNearestTrain(station, direction, false);
    }

    private Train getNearestTrain(Station station, Station direction, boolean skipLeftTrains) {

        Line line = stationToLine.get(station);

        Train train = null;
        int distance = Integer.MAX_VALUE;

        ConcurrentHashMap<Integer, Train> trainsMap = trainsPerLine.get(line);
        for (Train currentTrain : trainsMap.values()) {
            Station currentTrainStation = position.get(currentTrain);
            Station currentTrainDirection = getTrainDirection(currentTrain);
            int newDistance = getCountStationsTillTargetStation(currentTrainStation, station, direction, currentTrainDirection);

            if(skipLeftTrains && newDistance == 0 && getTrainInStation(station, direction)){
                continue;       // train is in station but left already
            }

            if(train == null) {
                train = currentTrain;
                distance = newDistance;
            }else if(newDistance < distance) {
                train = currentTrain;
            }
        }

        return train;
    }

    private Station getTrainDirection(Train train) {
        return this.direction.get(train);
    }

    /**
     * if target is after station the method assumes the target train is operated in the other direction
     *
     * @param station
     * @param target
     * @param directionOperated
     * @param directionTrain
     * @return
     */
    private int getCountStationsTillTargetStation(Station station, Station target, Station directionOperated, Station directionTrain) {

        // TODO: check it

        int cntStations = getAllStations(stationToLine.get(station)).size();

        if(directionOperated == directionTrain) {

            if(station == target)
                return 0;

            int cmpResult = stationCompare(station, target, directionOperated);

            if(cmpResult < 0)
                return getCountStationsBetween(station, target);

            // to the end --> all back --> to the target
            // station > target
            return getCountStationsBetween(station, directionOperated) + (cntStations <= 3 ? cntStations : cntStations + 1) + getCountStationsBetween(getOtherDirection(directionOperated), target);
        }else {

           // if(station == target)
             //   return getCountStationsBetween(station, directionTrain) + getCountStationsBetween(directionTrain, target);
          //      return cntStations <= 3 ? cntStations : cntStations + 1;

            // to the end --> to the station
            return getCountStationsBetween(station, directionTrain) + getCountStationsBetween(directionTrain, target);
        }
    }

    /**
     * compares station A to station B using their position on the line and the direction the train operates on
     * @param stationA
     * @param stationB
     * @param direction
     * @return
     */
    private int stationCompare(Station stationA, Station stationB, Station direction) {

        BiMap<Station, Integer> stationsMap = this.lineToStations.get(stationToLine.get(stationA));

        int idxA = stationsMap.get(stationA);
        int idxB = stationsMap.get(stationB);

        Line line = stationToLine.get(direction);

        // -->
        if(direction == getLastStation(line)) {
            return Integer.compare(idxA, idxB);
        }
        else {  // <--
            return Integer.compare(idxB, idxA);
        }
    }


    /**
     *
     * @param stationA
     * @param stationB
     * @return returns the amount of stations from station a to station b (e.g. 1 if station b follows station a, or station a follows station b)
     */
    private int getCountStationsBetween(Station stationA, Station stationB) {

        BiMap<Station, Integer> stationsMap = this.lineToStations.get(stationToLine.get(stationA));

        int idxA = stationsMap.get(stationA);
        int idxB = stationsMap.get(stationB);

        if(idxA > idxB)
            return idxA - idxB;
        return idxB - idxA;
    }

    /**
     *
     * @param line
     * @return all the stations on the line
     */
    private Set<Station> getAllStations(Line line) {
        return this.lineToStations.get(line).keySet();
    }

    /**
     *
     * @param line
     * @return list with 2 elements (both directions)
     */
    private List<Station> getDirections(Line line) {

        List<Station> directions = new LinkedList<>();

        directions.add(getFirstStation(line));
        directions.add(getLastStation(line));

        assert directions.size() == 2;
        return directions;
    }


    private void initU1() {
        Line u1 = createLine("U1", 3);

        Station reumannplatz = createStation(u1, "Reumannplatz", 1, 1);
        Station keplerplatz = createStation(u1, "Keplerplatz", 2, 2);
        Station hbf = createStation(u1, "Hauptbahnhof", 3, 3);
        Station taubstummengasse = createStation(u1, "Taubstummengasse", 4, 4);
        Station karlsplatz = createStation(u1, "Karlsplatz", 5, 5);
        Station stephansplatz = createStation(u1, "Stephansplatz", 6, 6);
        Station schwedenplatz = createStation(u1, "Schwedenplatz", 7, 7);
        Station nestroyplatz = createStation(u1, "Nestroyplatz", 8, 8);
        Station praterstern = createStation(u1, "Praterstern", 9, 9);
        Station vorgartenstraße = createStation(u1, "Vorgartenstraße", 10, 10);
        Station donauinsel = createStation(u1, "Donauinsel", 11, 11);
        Station leopoldau = createStation(u1, "Leopoldau", 12, 12);

        initHelperData(u1);

        // Leopoldau -> Raeumannplatz
        createTrain(u1, 1, leopoldau, reumannplatz, 0);
        createTrain(u1, 2, vorgartenstraße, reumannplatz, 0);
        createTrain(u1, 3, nestroyplatz, reumannplatz, 0);
        createTrain(u1, 4, stephansplatz, reumannplatz, 0);
        createTrain(u1, 5, taubstummengasse, reumannplatz, 0);
        createTrain(u1, 6, keplerplatz, reumannplatz, 0);

        // Raeumannplatz -> Leopoldau
        createTrain(u1, 7, reumannplatz, leopoldau, 0);
        createTrain(u1, 8, hbf, leopoldau, 0);
        createTrain(u1, 9, karlsplatz, leopoldau, 0);
        createTrain(u1, 10, schwedenplatz, leopoldau, 0);
        createTrain(u1, 11, praterstern, leopoldau, 0);
        createTrain(u1, 12, donauinsel, leopoldau, 0);

        updateArrivalTimes(u1);
    }

    private void initHelperData(Line line) {
        for (Station station : getAllStations(line)) {
            for(Station direction : getDirections(line)) {
                setTrainInStation(station, direction, false);
                setPlannedArrival(station, direction, currentTime.plusMinutes(getTimeToTravelPerStation(line)));
            }
        }
    }

    private void updateArrivalTimes(Line line) {
        for (Station station : getAllStations(line)) {
            for (Station direction : getDirections(line)) {

                DateTime estimatedArrival = currentTime;

                if(isFree(station, direction)) {
                    Train nearestTrain = getNearestTrain(station, direction, true);
                    int countStationsTillTargetStation = getCountStationsTillTargetStation(position.get(nearestTrain), station, direction, this.direction.get(nearestTrain));

                    // =0: just arrived at current station
                    // >0: on the way to the station
                    // <0: ready to leave station
                    Integer waitTime = waitingTime.get(nearestTrain);

                    if(waitTime > 0) {
                  //      countStationsTillTargetStation = countStationsTillTargetStation - 1;
                    }

                    estimatedArrival = estimatedArrival.plusMinutes(countStationsTillTargetStation * getTimeToTravelPerStation(line) + waitTime);
                }

                setEstimatedArrival(station, direction, estimatedArrival);
            }
        }
    }

    /**
     * IMPORTANT: the amount of (stations times directions) modulo the amount of trains operating needs to be zero (e.g 20 stations, 10 trains is ok, 20 stations 11 trains not!)
     * @param line
     * @param number
     * @param currentStation
     * @param endStation
     * @param waitTime
     * @return
     */
    private Train createTrain(Line line, int number, Station currentStation, Station endStation, int waitTime) {
        Train train = new Train();

        train.setNumber(number);

        this.trains.put(number, train);
        this.position.put(train, currentStation);
        //int waitTime = random.nextInt(getTimeToTravelPerStation(line));       //up to TIME_TO_TRAVEL_PER_STATION minutes till next station reached
        //setLastTrain(currentStation, endStation, currentTime.minusMinutes(getTimeToTravelPerStation(line) - waitTime));
        setWaitTime(train, waitTime);
        this.direction.put(train, endStation);

        ConcurrentHashMap<Integer, Train> trainsOnThisLine = trainsPerLine.get(line);
        if(trainsOnThisLine == null) {
            trainsOnThisLine = new ConcurrentHashMap<>();
            trainsPerLine.put(line, trainsOnThisLine);
        }
        trainsOnThisLine.put(number, train);

        setPlannedArrival(currentStation, endStation, currentTime);
        setAdditionalWaitTime(train, 0);
        setTrainInStation(currentStation, endStation, true);
        return train;
    }

    /**
     * sets the time when the last train was in the station
     * @param station
     * @param direction
     * @param time
     */
    private void setLastTrain(Station station, Station direction, DateTime time) {
        ConcurrentHashMap<Station, DateTime> stationMap = this.lastTrain.get(station);

        if(stationMap == null) {
            stationMap = new ConcurrentHashMap<>();
            this.lastTrain.put(station, stationMap);
        }
        stationMap.put(direction, time);
    }

    private DateTime getLastTrain(Station station, Station direction) {
        return lastTrain.get(station).get(direction);
    }

    private void setEstimatedArrival(Station station, Station direction, DateTime time) {
        ConcurrentHashMap<Station, DateTime> stationMap = this.estimatedArrival.get(station);

        if(stationMap == null) {
            stationMap = new ConcurrentHashMap<>();
            this.estimatedArrival.put(station, stationMap);
        }
        stationMap.put(direction, time);
    }

    private DateTime getEstimatedArrival(Station station, Station direction) {
        return estimatedArrival.get(station).get(direction);
    }

    private void setTrainInStation(Station station, Station direction, Boolean inStation) {
        ConcurrentHashMap<Station, Boolean> stationMap = this.trainInStation.get(station);

        if(stationMap == null) {
            stationMap = new ConcurrentHashMap<>();
            this.trainInStation.put(station, stationMap);
        }
        stationMap.put(direction, inStation);
    }

    private Boolean getTrainInStation(Station station, Station direction) {
        return trainInStation.get(station).get(direction);
    }

    private void setAdditionalWaitTime(Train train, int waitTime) {
        this.additonalWaitTime.put(train, waitTime);
    }

    private int getAdditionalWaitTime(Train train) {
        return this.additonalWaitTime.get(train);
    }

    private void setPlannedArrival(Station station, Station direction, DateTime time) {
        ConcurrentHashMap<Station, DateTime> stationMap = this.plannedArrival.get(station);

        if(stationMap == null) {
            stationMap = new ConcurrentHashMap<>();
            this.plannedArrival.put(station, stationMap);
        }
        stationMap.put(direction, time);
    }

    private DateTime getPlannedArrival(Station station, Station direction) {
        return plannedArrival.get(station).get(direction);
    }


    private Station getCurrentStation(Train train) {
        return this.position.get(train);
    }


    //
    // CREATE DEMO DATA
    //


    /**
     *
     * @param line
     * @param name
     * @param number
     * @return
     */
    private Station createStation(Line line, String name, int number, int position) {

        // create station
        Station station = new Station();
        station.setName(name);
        station.setNumber(number);
        station.setPosition(position);
        stations.put(name, station);

        // add station to line
        BiMap<Station, Integer> stationsForLine = lineToStations.get(line);
        if(stationsForLine == null)        {
            stationsForLine =  Maps.synchronizedBiMap(HashBiMap.create());
            lineToStations.put(line, stationsForLine);
        }
        stationsForLine.put(station, number);

        stationToLine.put(station, line);

        return station;
    }

    private Line createLine(String name, int timeToTravel) {
        Line line = new Line();
        line.setName(name);
        lines.put(name, line);

        // set default values for dynamic data
        lineError.put(line, false);
        this.lineToStations.put(line, Maps.synchronizedBiMap(HashBiMap.create()));
        this.timeToTravel.put(line, timeToTravel);

        return line;
    }

    public Collection<Line> getLines() {
        return this.lines.values();
    }

    public List<Station> getDirections(String lineName) {
        Line line = lines.get(lineName);
        if(line == null)    return null;
        return getDirections(line);
    }

    public List<Station> getStations(String lineName) {
        Line line = lines.get(lineName);
        if(line == null)    return null;
        return Lists.newLinkedList(getAllStations(line));
    }
}
