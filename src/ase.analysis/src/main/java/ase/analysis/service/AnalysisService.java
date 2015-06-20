package ase.analysis.service;

import ase.shared.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Michael on 19.06.2015.
 */
@Component
public class AnalysisService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Scheduled(fixedDelay = 3000)
    public void sendMessage() {
        System.out.println("Send Notification");
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setEmail("hugo@gmx.at");
        jmsTemplate.convertAndSend("queue:notifications", notificationDTO);
    }
}
