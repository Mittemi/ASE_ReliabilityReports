package ase.notify.controller;


import ase.shared.dto.NotificationDTO;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by Michael on 18.06.2015.
 */
@Component
public class NotificationController {

    @JmsListener(destination = "notifications")
    public void sendNotification(NotificationDTO notificationDTO){
        System.out.println("Send Notification!");
        if(notificationDTO != null) {
            if(notificationDTO.getEmail() != null)
                System.out.println(" TO: " + notificationDTO.getEmail());
        }
    }
}
