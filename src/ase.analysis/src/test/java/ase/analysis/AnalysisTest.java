package ase.analysis;

import ase.analysis.analysis.AnalysisService;
import ase.shared.dto.AnalysisRequestDTO;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Michael on 22.06.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AnalysisApplication.class)
@WebIntegrationTest()
public class AnalysisTest {

    @Autowired
    private AnalysisService analysisService;

    @Test
    public void testAnalysisU1(){

        AnalysisRequestDTO analysisRequestDTO = new AnalysisRequestDTO();
        analysisRequestDTO.setFrom(new DateTime(2015, 6, 1, 8, 0).toDate());
        analysisRequestDTO.setTo(new DateTime(2015, 6, 1, 10, 50).toDate());
        analysisRequestDTO.setLine("U1");
        analysisRequestDTO.setStationFrom("Karlsplatz");
        analysisRequestDTO.setStationTo("Donauinsel");

        analysisService.jmsAnalyseTarget(analysisRequestDTO);
    }
}
