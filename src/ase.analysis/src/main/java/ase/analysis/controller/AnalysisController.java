package ase.analysis.controller;

import ase.analysis.analysis.AnalysisService;
import ase.analysis.analysis.prioritizedMessaging.MessagePriority;
import ase.shared.dto.AnalysisRequestDTO;
import ase.shared.dto.AnalysisResponseDTO;
import ase.shared.dto.ReportMetadataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by Michael on 21.06.2015.
 */
@RestController
@RequestMapping(value = "/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @RequestMapping(value = "/request/{priority}/", method = RequestMethod.POST, produces = "application/json")
    public AnalysisResponseDTO requestAnalysis(@RequestBody AnalysisRequestDTO analysisRequestDTO, @PathVariable("priority") String priority) {

        MessagePriority messagePriority = MessagePriority.valueOf(priority);

        ReportMetadataDTO reportMetadata = new ReportMetadataDTO();
        reportMetadata.setRequestedAt(new Date());
        reportMetadata.setPriority(messagePriority.getValue());
        reportMetadata.setUserId(analysisRequestDTO.getUserId());

        analysisRequestDTO.setReportMetadata(reportMetadata);

        // queue the request for analysis. one of our analysis servers is going to take care of the actual work
        return analysisService.queueForAnalysis(analysisRequestDTO, messagePriority);
    }

}
