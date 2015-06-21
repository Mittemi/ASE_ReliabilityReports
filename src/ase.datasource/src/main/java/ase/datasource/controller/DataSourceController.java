package ase.datasource.controller;

import ase.datasource.simulation.DataSimulation;
import ase.shared.model.simulation.Line;
import ase.shared.model.simulation.RealtimeData;
import ase.shared.model.simulation.Station;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


/**
 * Created by Michael on 20.06.2015.
 */
@Controller
@RequestMapping(value = "/simulation")
public class DataSourceController {

    @Autowired
    private DataSimulation dataSimulation;

    //@RequestMapping(value = "/{line}/{station}/{direction}/")
//    @RequestMapping(value = "/data/{line}/")
//    @ResponseBody
//    public List<RealtimeData> realtimeData(
//            @PathVariable String line/*, @PathVariable String station, @PathVariable String direction*/) {
//
//        RealtimeData realtimeData = new RealtimeData();
//        realtimeData.add(linkTo(methodOn(DataSourceController.class).realtimeData(line, station, direction)).withSelfRel());
//
//
//
//        return new ResponseEntity<>(realtimeData, HttpStatus.OK);
//        return dataSimulation.move(line);
//    }

//    @RequestMapping(value = "/")
//    public String simulate() {
//        dataSimulation.move("U1");
//        return "OK";
//    }

    @RequestMapping(value = "/lines/")
    public Resource<Collection<Line>> getLines() {
        Collection<Line> lines = dataSimulation.getLines();
        return new Resource<>(lines);
    }

    @RequestMapping(value = "/directions/{line}/")
    public Resource<List<Station>> getDirections(@PathVariable String line) {
        List<Station> directions = dataSimulation.getDirections(line);
        return new Resource<>(directions);
    }

    @RequestMapping(value = "/stations/{line}/")
    public Resource<List<Station>> getAllStations(@PathVariable String line) {
        List<Station> stations = dataSimulation.getStations(line);
        return new Resource<>(stations);
    }

    @RequestMapping(value = "/time", produces = "application/json")
    public String currentTime() {
        return dataSimulation.getCurrentTime().toString();
    }
}
