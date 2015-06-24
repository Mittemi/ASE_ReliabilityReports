package ase.apiGateway.controller;

import ase.shared.commands.CommandFactory;
import ase.shared.dto.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Michael on 23.06.2015.
 */
@RestController
@RequestMapping(value = "/report")
public class ReportController {

    @Autowired
    private CommandFactory commandFactory;

    @RequestMapping(value = "/view/{reportId}")
    @HystrixCommand(fallbackMethod = "getReportFallback")
    public ReportDTO getReport(@PathVariable String reportId) {
        return commandFactory.getReportByIdCommand(reportId).getSingleResult();
    }

    /* Hystrix fallback */
    public ReportDTO getReportFallback(String reportId) {
        return null;
    }

    @RequestMapping(value = "/metadata/{reportId}")
    @HystrixCommand(fallbackMethod = "getReportMetadataFallback")
    public DataConcernDTO getReportMetadata(@PathVariable String reportId) {
        return commandFactory.getDataConcernsCommand(reportId).getResult();
    }

    /* Hystrix fallback */
    public ReportMetadataDTO getReportMetadataFallback(String reportId) {
        return null;
    }

    @RequestMapping(value = "/request/{priority}/", method = RequestMethod.POST)
    @HystrixCommand(fallbackMethod = "requestReportFallback")
    public AnalysisResponseDTO requestReport(@RequestBody AnalysisRequestDTO analysisRequestDTO, @PathVariable String priority) {

        return commandFactory.requestReportCommand(analysisRequestDTO, priority).getResult();
    }

    /* Hystrix fallback */
    public AnalysisResponseDTO requestReportFallback(AnalysisRequestDTO analysisRequestDTO, String priority) {
        return null;
    }
}
