package ase.apiGateway.controller;

import ase.shared.commands.CommandFactory;
import ase.shared.model.notification.Notification;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 23.06.2015.
 */
@RestController
@RequestMapping(value = "/notification/")
public class NotificationController {

    @Autowired
    private CommandFactory commandFactory;

    @RequestMapping(value = "/{userId}/")
    @HystrixCommand(fallbackMethod = "getNotificationsFallback")
    public List<Notification> getNotifications(@PathVariable("userId") String userId) {
        return commandFactory.getNotificationsByEmailCommand(userId).toList();
    }

    /* Hystrix fallback */
    public List<Notification> getNotificationsFallback(String userId) {
        return new LinkedList<>();
    }
}
