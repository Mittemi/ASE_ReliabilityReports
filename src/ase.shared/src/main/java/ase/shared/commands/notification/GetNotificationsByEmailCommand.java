package ase.shared.commands.notification;

import ase.shared.commands.GenericHATEOSGetCommand;
import ase.shared.commands.resttypes.NotificationRestWrapper;
import ase.shared.commands.resttypes.ReportDTORestWrapper;
import ase.shared.dto.ReportDTO;
import ase.shared.model.notification.Notification;
import com.google.common.collect.ImmutableMap;
import org.springframework.hateoas.client.Traverson;

/**
 * Created by Michael on 23.06.2015.
 */
public class GetNotificationsByEmailCommand extends GenericHATEOSGetCommand<NotificationRestWrapper, Notification> {

    private String email;

    public GetNotificationsByEmailCommand(String url, String email) {
        super(NotificationRestWrapper.class, url);
        this.email = email;
    }

    @Override
    protected Traverson.TraversalBuilder initBuilder(Traverson traverson) {
        return traverson.follow("storedNotifications", "search", "findByEmail").withTemplateParameters(ImmutableMap.of("email", email));
    }
}
