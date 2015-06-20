package ase.datasource.controller;

import ase.datasource.simulation.DataSimulation;
import ase.shared.model.simulation.RealtimeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


/**
 * Created by Michael on 20.06.2015.
 */
@Controller
public class DataSourceController {

    @Autowired
    private DataSimulation dataSimulation;

//    @RequestMapping(value = "/{line}/{station}/{direction}/")
//    @ResponseBody
//    public HttpEntity<RealtimeData> realtimeData(
//            @PathVariable String line, @PathVariable String station, @PathVariable String direction) {
//
//        RealtimeData realtimeData = new RealtimeData();
//        realtimeData.add(linkTo(methodOn(DataSourceController.class).realtimeData(line, station, direction)).withSelfRel());
//
//
//
//        return new ResponseEntity<>(realtimeData, HttpStatus.OK);
//    }

    @RequestMapping(value = "/")
    public String simulate() {
        dataSimulation.move("U1");
        return "OK";
    }
}
