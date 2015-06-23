package ase.shared.dto;

import ase.shared.model.notification.Notification;

import java.io.Serializable;

/**
 * Created by Michael on 19.06.2015.
 */
public class NotificationDTO extends Notification implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
