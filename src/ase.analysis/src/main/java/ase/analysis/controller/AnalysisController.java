package ase.analysis.controller;

import ase.analysis.analysis.AnalysisService;
import ase.analysis.analysis.prioritizedMessaging.MessagePriority;
import ase.shared.commands.CommandFactory;
import ase.shared.dto.AnalysisRequestDTO;
import ase.shared.dto.AnalysisResponseDTO;
import ase.shared.model.ReportMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by Michael on 21.06.2015.
 */
@RestController
@RequestMapping(value = "/analysis")
public class AnalysisController {

    @Autowired
    private CommandFactory commandFactory;

    @Autowired
    private AnalysisService analysisService;

    @RequestMapping(value = "/request", method = RequestMethod.POST, produces = "application/json")
    public AnalysisResponseDTO requestAnalysis(@RequestBody AnalysisRequestDTO analysisRequestDTO) {

        AnalysisResponseDTO analysisResponseDTO = new AnalysisResponseDTO();

        analysisResponseDTO.setOk(true);

        ReportMetadata reportMetadata = new ReportMetadata();
        reportMetadata.setRequestedAt(new Date());

        analysisRequestDTO.setReportMetadata(reportMetadata);

        if ("Karlsplatz".equals(analysisRequestDTO.getStationFrom())) {
            analysisService.queueForAnalysis(analysisRequestDTO, MessagePriority.High);
        } else {
            analysisService.queueForAnalysis(analysisRequestDTO, MessagePriority.Low);
        }

        return analysisResponseDTO;
    }

}
