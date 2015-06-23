package ase.apiGateway.controller;

import ase.shared.commands.CommandFactory;
import ase.shared.dto.DataConcernDTO;
import ase.shared.dto.ReportDTO;
import ase.shared.dto.ReportMetadataDTO;
import ase.shared.model.analysis.Report;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael on 23.06.2015.
 */
@RestController
public class ReportController {

    @Autowired
    private CommandFactory commandFactory;

    @RequestMapping(value = "/report/{reportId}")
    @HystrixCommand(fallbackMethod = "getReportFallback")
    public ReportDTO getReport(@PathVariable String reportId) {
        return commandFactory.getReportByIdCommand(reportId).getSingleResult();
    }

    /* Hystrix fallback */
    public ReportDTO getReportFallback(String reportId) {
        return null;
    }

    @RequestMapping(value = "/metadata/{reportId}")
    //@HystrixCommand(fallbackMethod = "getReportMetadataFallback")
    public DataConcernDTO getReportMetadata(@PathVariable String reportId) {
        return commandFactory.getDataConcernsCommand(reportId).getResult();
    }

    /* Hystrix fallback */
    public ReportMetadataDTO getReportMetadataFallback(String reportId) {
        return null;
    }
}
