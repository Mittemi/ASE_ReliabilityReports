package ase.apiGateway.controller;

import ase.shared.commands.CommandFactory;
import ase.shared.dto.ReportMetadataDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael on 24.06.2015.
 */
@RestController
@RequestMapping(value = "/internals")
public class InternalsController {

    @Autowired
    private CommandFactory commandFactory;

    @RequestMapping(value = "/metadata/{reportId}")
    @HystrixCommand(fallbackMethod = "getReportMetadataFallback")
    public ReportMetadataDTO getReportMetadata(@PathVariable String reportId) {
        return commandFactory.getReportMetadataByReportIdCommand(reportId).getSingleResult();
    }

    /* Hystrix fallback */
    public ReportMetadataDTO getReportMetadataFallback(String reportId) {
        return null;
    }

//    @RequestMapping(value = "/reports")
//    public List<String> getReportIds() {
//
//    }
}
