package ase.shared.commands.notification;

import ase.shared.commands.GenericRESTCreateCommand;
import ase.shared.model.notification.Notification;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 23.06.2015.
 */
public class CreateNotificationCommand extends GenericRESTCreateCommand<Notification>{
    private String url;
    private Notification body;

    public CreateNotificationCommand(String url, Notification body) {
        this.url = url;
        this.body = body;
    }

    @Override
    protected RequestEntity<Notification> getRequest() {
        return new RequestEntity<>(body, HttpMethod.POST, URI.create(url + "/storedNotifications/"));
    }
}
