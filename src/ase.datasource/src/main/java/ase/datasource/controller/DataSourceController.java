package ase.datasource.controller;

import ase.datasource.simulation.DataSimulation;
import ase.shared.dto.LineInfoDTO;
import ase.shared.model.simulation.Line;
import ase.shared.model.simulation.Station;
import ase.shared.model.simulation.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Created by Michael on 20.06.2015.
 */
@RestController
@RequestMapping(value = "/static")
public class DataSourceController {

    @Autowired
    private DataSimulation dataSimulation;

    @RequestMapping(value = "/lines/")
    public Collection<Line> getLines() {
        Collection<Line> lines = dataSimulation.getLines();
        return lines;
    }

    @RequestMapping(value = "/directions/{line}/")
    public List<Station> getDirections(@PathVariable String line) {
        List<Station> directions = dataSimulation.getDirections(line);
        return directions;
    }

    @RequestMapping(value = "/stations/{line}/")
    public List<Station> getAllStations(@PathVariable String line) {
        List<Station> stations = dataSimulation.getStations(line);
        return stations;
    }

    @RequestMapping(value="/stations/{line}/between/{a}/{b}/")
    public List<Station> getStationsBetween(@PathVariable String line, @PathVariable String a, @PathVariable String b) {
        List<Station> allStations = getAllStations(line);

        Station stationA = allStations.stream().filter(x->x.getName().equals(a)).findFirst().get();
        Station stationB = allStations.stream().filter(x->x.getName().equals(b)).findFirst().get();

        int min = Math.min(stationA.getPosition(), stationB.getPosition());
        int max = Math.max(stationA.getPosition(), stationB.getPosition());
        return allStations.stream().filter(x->x.getPosition() >= min && x.getPosition() <= max).collect(Collectors.toList());
    }

    @RequestMapping(value = "/time", produces = "application/json")
    public String currentTime() {
        return dataSimulation.getCurrentTime().toString();
    }

    @RequestMapping(value = "/lineinfo/{line}/")
    public LineInfoDTO getLineInfo(String line) {
        int time = dataSimulation.getTimeToTravelPerStation(line);
        LineInfoDTO lineInfoDTO = new LineInfoDTO();
        lineInfoDTO.setTimeBetweenTrains(time);
        lineInfoDTO.setTrains(dataSimulation.getTrains(line).stream().map(Train::getNumber).collect(Collectors.toList()));

        // obviously this is also faked but required for concern evaluation
        Random random = new Random(new Date().getTime());
        lineInfoDTO.setSamplingRate(30 + random.nextInt(30));

        return lineInfoDTO;
    }
}
