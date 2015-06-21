package ase.analysis.analysis;

import ase.analysis.Constants;
import ase.analysis.analysis.prioritizedMessaging.MessagePriority;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedJmsTemplate;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedMessage;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedMessageCreator;
import ase.shared.dto.AnalysisRequestDTO;
import ase.shared.dto.AnalysisResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by Michael on 21.06.2015.
 */
@Component
public class AnalysisService {

    @Autowired
    private PrioritizedJmsTemplate prioritizedJmsTemplate;

    public AnalysisResponseDTO queueForAnalysis(AnalysisRequestDTO analysisRequestDTO, MessagePriority messagePriority) {

        System.out.println("Analysis started...");

        AnalysisResponseDTO analysisResponseDTO = new AnalysisResponseDTO();
        analysisResponseDTO.setOk(true);

        //todo: validate request

        // queue for analysis if request is valid
        if(analysisResponseDTO.isOk()) {

            try {
                prioritizedJmsTemplate.send(new PrioritizedMessageCreator(new PrioritizedMessage<Object>(analysisRequestDTO, messagePriority)));
            } catch (JmsException e) {
                e.printStackTrace();
                analysisResponseDTO.setOk(false);
                analysisResponseDTO.setMessage("Queueing for analysis failed! Please try again");
            }
        }

        System.out.println("Analysis finished...");
        return analysisResponseDTO;
    }


    @JmsListener(destination = Constants.ANALYSIS_QUEUE_NAME)
    public void jmsAnalyseTarget(AnalysisRequestDTO analysisRequestDTO) {
        System.out.println("Analysis request received (JMS)");
        System.out.println(analysisRequestDTO.getStationFrom());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        System.out.println("Analysis finished (jms)");
    }

}
