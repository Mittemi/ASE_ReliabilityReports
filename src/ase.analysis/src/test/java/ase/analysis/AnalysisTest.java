package ase.analysis;

import ase.analysis.analysis.AnalysisService;
import ase.analysis.analysis.prioritizedMessaging.MessagePriority;
import ase.analysis.controller.AnalysisController;
import ase.shared.dto.AnalysisRequestDTO;
import ase.shared.dto.ReportMetadataDTO;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by Michael on 22.06.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AnalysisApplication.class)
@WebIntegrationTest(randomPort = true)
public class AnalysisTest {

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private AnalysisController controller;

    @Test
    public void testAnalysisU1(){

        AnalysisRequestDTO analysisRequestDTO = new AnalysisRequestDTO();
        analysisRequestDTO.setFrom(new DateTime(2015, 6, 1, 8, 0).toDate());
        analysisRequestDTO.setTo(new DateTime(2015, 6, 1, 10, 50).toDate());
        analysisRequestDTO.setLine("U1");
        analysisRequestDTO.setStationFrom("Karlsplatz");
        analysisRequestDTO.setStationTo("Donauinsel");
        analysisRequestDTO.setUserId("Unit-Test");

        ReportMetadataDTO reportMetadata = new ReportMetadataDTO();
        reportMetadata.setRequestedAt(new Date());
        reportMetadata.setUserId(analysisRequestDTO.getUserId());
        reportMetadata.setPriority(MessagePriority.Medium.getValue());
        analysisRequestDTO.setReportMetadata(reportMetadata);

        analysisService.jmsAnalyseTarget(analysisRequestDTO);
    }

    @Autowired
    JmsTemplate jmsTemplate;

    @Test
    public void testJms() {
        AnalysisRequestDTO analysisRequestDTO = new AnalysisRequestDTO();
        analysisRequestDTO.setFrom(new DateTime(2015, 6, 1, 8, 0).toDate());
        analysisRequestDTO.setTo(new DateTime(2015, 6, 1, 10, 50).toDate());
        analysisRequestDTO.setLine("U1");
        analysisRequestDTO.setStationFrom("Karlsplatz");
        analysisRequestDTO.setStationTo("Donauinsel");
        analysisRequestDTO.setUserId("Unit-Test");

        ReportMetadataDTO reportMetadata = new ReportMetadataDTO();
        reportMetadata.setRequestedAt(new Date());
        reportMetadata.setUserId(analysisRequestDTO.getUserId());
        reportMetadata.setPriority(MessagePriority.Medium.getValue());
        analysisRequestDTO.setReportMetadata(reportMetadata);

        controller.requestAnalysis(analysisRequestDTO, MessagePriority.Low.name());
        controller.requestAnalysis(analysisRequestDTO, MessagePriority.Low.name());
        analysisRequestDTO.setStationTo("Leopoldau");
        controller.requestAnalysis(analysisRequestDTO, MessagePriority.Low.name());
        analysisRequestDTO.setStationTo("Schwedenplatz");
        controller.requestAnalysis(analysisRequestDTO, MessagePriority.Low.name());
        analysisRequestDTO.setUserId("High");
        controller.requestAnalysis(analysisRequestDTO, MessagePriority.High.name());
    }
}
