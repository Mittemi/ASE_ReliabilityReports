package ase.datasource.simulation;

import ase.shared.model.simulation.Line;
import ase.shared.model.simulation.RealtimeData;
import ase.shared.model.simulation.Station;
import ase.shared.model.simulation.Train;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Michael on 20.06.2015.
 */
public class DataSimulation {

    // begin static data

    private Random random = new Random(1);      //fixed to keep things predictable!

    private DateTime currentTime = new DateTime(2015,6,1,8,0,0);



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

    /**
     * Train -> delay
     */
    //private final ConcurrentHashMap<Train, Integer> delayTrain;

    /**
     * Train -> amount of time to wait till next move
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

        initU1();
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

        // arrived at station, nothing to do
        if(waitTime == 0) {
            System.out.println(currentTime.toString("dd.MM HH:mm") +  ": Train " + trainNumber + " arrived at " + currentStation.getName());
            setWaitTime(train, -1);
        }else if(waitTime == -1) {
            Station nextStation = getNextStation(currentStation, direction);

            // check if we need to turn on the current station (change direction, happens immediately )
            if(nextStation == null) {
                Station newDirection = getOtherDirection(direction);
                // train stays at the same station but changes direction
                moveTrainIfPossible(train, currentStation, newDirection, currentStation);
            } else {
                moveTrainIfPossible(train, currentStation, direction, nextStation);
            }
        }else {
            setWaitTime(train, waitTime - 1);
        }
    }

    private void moveTrainIfPossible(Train train, Station currentStation, Station direction, Station nextStation) {
        if(isFree(nextStation, direction)) {
            this.position.put(train, nextStation);
            this.direction.put(train, direction);
            System.out.println(currentTime.toString("dd.MM HH:mm") +  ": Train " + train.getNumber() + " left " + currentStation.getName());
            setWaitTime(train, random.nextInt(2) + 1);
        } else {
            System.out.println(currentTime.toString("dd.MM HH:mm") +  ": Train " + train.getNumber() + " can't leave station " + currentStation.getName() + "! Station: " + nextStation.getName() + " not free in direction " + direction.getName());
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

        BiMap<Station, Integer> stations = this.lineToStations.get(this.stationToLine.get(direction));
        Station station;

        if(stations.get(direction) == 1) {
            station = stations.inverse().get(stations.size());
        }
        else {
            station = stations.inverse().get(1);
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
    private boolean isFree(Station station, Station direction){
        for (Train train : getTrainsInDirection(direction)) {
            if(this.position.get(train) == station) return false;
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

        Integer idxDirection = stations.get(direction);

        // -->
        if(idxDirection != 1) {
            return stations.inverse().get(position + 1);
        } else  {   // <--
            return stations.inverse().get(position - 1);
        }
    }

    public List<RealtimeData> move(String lineName) {

        currentTime = currentTime.plusMinutes(1);

        List<RealtimeData> result = new LinkedList<>();
        Line line = lines.get(lineName);

        ConcurrentHashMap<Integer, Train> trains = this.trainsPerLine.get(line);


        List<Station> directions = getDirections(line);

        for(Station station : getAllStations(line)) {

            for(Station direction : directions) {

                RealtimeData realtimeData = new RealtimeData();

                realtimeData.setLine(line);
                realtimeData.setDirection(direction);
                realtimeData.setError(false);
                realtimeData.setStation(station);
                Train nearestTrain = getNearestTrain(station, direction);

                result.add(realtimeData);
            }
        }

        for (Train train : trains.values()) {
            tick(train.getNumber());
        }

        //Integer numberStart = stations.keySet().stream().min((key, val) -> key).get();

        //Integer numberTurn =

        //realtimeData.setDirection();

        //return realtimeData;
        return null;
    }

    private Train getNearestTrain(Station station, Station direction) {

        //TODO: this
        BiMap<Station, Integer> stationsMap = lineToStations.get(stationToLine.get(station));

        return null;
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
    private int getCountStationsTillStation(Station station, Station target, Station directionOperated, Station directionTrain) {

        // TODO: this

        int cntStations = getAllStations(stationToLine.get(station)).size();

        // same station
        if(station == target) {
            if(directionOperated == directionTrain) {
                return 0;
            }
            // TODO: check this
            return cntStations <= 3 ? cntStations : cntStations + 1;
        }

        int cmpResult = stationCompare(station, target, directionOperated);

        // -->
        if(cmpResult < 0) {
            if(directionOperated == directionTrain) {
                return getCountStationsBetween(station, target);
            }
            // we need to complete the current direction + the way to the target (changing the direction happens immediately )
            // start2station + target2end
            return getCountStationsBetween(directionTrain, target) + getCountStationsBetween(target, getOtherDirection(directionOperated));
        }
        else {  // <--

            if(directionOperated == directionTrain) {

            }
            return getCountStationsBetween(station, target);
        }



        // the target is going to be reached somewhere in the future (in the supplied direction)
//        if(cmpResult < 0)   {
//
//            int countStationsBetween = getCountStationsBetween(station, target);
//
//            // we need to complete the current direction before starting the other one
//            //if(directionTrain != directionOperated)
//            //return countStationsBetween + ;
//        }

//        return 0;
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

        // -->
        if(stationsMap.get(direction) != 1) {
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
        BiMap<Station, Integer> stationsMap = this.lineToStations.get(line);

        directions.add(stationsMap.inverse().get(1));
        directions.add(stationsMap.inverse().get(stationsMap.size()));

        assert directions.size() == 2;
        return directions;
    }


    private void initU1() {
        Line u1 = createLine("U1");

        Station reumannplatz = createStation(u1, "Reumannplatz", 1);
        Station keplerplatz = createStation(u1, "Keplerplatz", 2);
        Station hbf = createStation(u1, "Hauptbahnhof", 3);
        Station taubstummengasse = createStation(u1, "Taubstummengasse", 4);
        Station karlsplatz = createStation(u1, "Karlsplatz", 5);
        Station stephansplatz = createStation(u1, "Stephansplatz", 6);
        Station schwedenplatz = createStation(u1, "Schwedenplatz", 7);
        Station nestroyplatz = createStation(u1, "Nestroyplatz", 8);
        Station praterstern = createStation(u1, "Praterstern", 9);
        Station vorgartenstraße = createStation(u1, "Vorgartenstraße", 10);
        Station donauinsel = createStation(u1, "Donauinsel", 11);
        Station leopoldau = createStation(u1, "Leopoldau", 12);

        // Leopoldau -> Raeumannplatz
        createTrain(u1, 1, leopoldau, reumannplatz);
        createTrain(u1, 2, vorgartenstraße, reumannplatz);
        createTrain(u1, 3, nestroyplatz, reumannplatz);
        createTrain(u1, 4, stephansplatz, reumannplatz);
        createTrain(u1, 5, taubstummengasse, reumannplatz);
        createTrain(u1, 6, keplerplatz, reumannplatz);

        // Raeumannplatz -> Leopoldau
        createTrain(u1, 7, reumannplatz, leopoldau);
        createTrain(u1, 8, hbf, leopoldau);
        createTrain(u1, 9, karlsplatz, leopoldau);
        createTrain(u1, 10, schwedenplatz, leopoldau);
        createTrain(u1, 11, praterstern, leopoldau);
        createTrain(u1, 12, donauinsel, leopoldau);
    }

    private Train createTrain(Line line, int number, Station currentStation, Station endStation) {
        Train train = new Train();

        train.setNumber(number);

        this.trains.put(number, train);
        this.position.put(train, currentStation);
        int waitTime = random.nextInt(3);       //up to 3 minutes till next station reached
        setLastTrain(currentStation, endStation, currentTime.minusMinutes(3 - waitTime));
        setWaitTime(train, waitTime);
        this.direction.put(train, endStation);

        ConcurrentHashMap<Integer, Train> trainsOnThisLine = trainsPerLine.get(line);
        if(trainsOnThisLine == null) {
            trainsOnThisLine = new ConcurrentHashMap<>();
            trainsPerLine.put(line, trainsOnThisLine);
        }
        trainsOnThisLine.put(number, train);

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
    private Station createStation(Line line, String name, int number) {

        // create station
        Station station = new Station();
        station.setName(name);
        station.setNumber(number);
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

    private Line createLine(String name) {
        Line line = new Line();
        line.setName(name);
        lines.put(name, line);

        // set default values for dynamic data
        lineError.put(line, false);
        this.lineToStations.put(line, Maps.synchronizedBiMap(HashBiMap.create()));

        return line;
    }
}
