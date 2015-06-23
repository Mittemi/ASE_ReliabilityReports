package ase.notify.model;

import org.springframework.data.annotation.Id;

/**
 * Created by Michael on 23.06.2015.
 */
public class StoredNotification extends ase.shared.model.notification.Notification {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
