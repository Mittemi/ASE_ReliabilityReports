package ase.apiGateway.controller;

import ase.shared.ASEModelMapper;
import ase.shared.commands.CommandFactory;
import ase.shared.dto.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Michael on 23.06.2015.
 */
@RestController
@RequestMapping(value = "/report")
public class ReportController {

    @Autowired
    private CommandFactory commandFactory;

    @Autowired
    private ASEModelMapper modelMapper;

    @RequestMapping(value = "/view/{reportId}")
    @HystrixCommand(fallbackMethod = "getReportFallback")
    public PublicReportDTO getReport(@PathVariable String reportId) {
        ReportDTO reportDTO = commandFactory.getReportByIdCommand(reportId).getSingleResult();

        if(reportDTO == null)   {
            return null;
        }

        return modelMapper.map(reportDTO, PublicReportDTO.class);
    }

    /* Hystrix fallback */
    public PublicReportDTO getReportFallback(String reportId) {
        return null;
    }

    @RequestMapping(value = "/metadata/{reportId}")
    @HystrixCommand(fallbackMethod = "getReportMetadataFallback")
    public DataConcernListDTO getReportMetadata(@PathVariable String reportId) {
        return commandFactory.getDataConcernsCommand(reportId).getResult();
    }

    /* Hystrix fallback */
    public DataConcernListDTO getReportMetadataFallback(String reportId) {
        return null;
    }

    @RequestMapping(value = "/test/{priority}/", method = RequestMethod.POST)
    @HystrixCommand(fallbackMethod = "requestReportFallback")
    public AnalysisRequestDTO roundTrip(@RequestBody AnalysisRequestDTO analysisRequestDTO, @PathVariable String priority) {

        return analysisRequestDTO;
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
