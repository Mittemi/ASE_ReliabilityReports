package ase.shared.dto;

import java.io.Serializable;

/**
 * Created by Michael on 19.06.2015.
 */
public class NotificationDTO implements Serializable {

    private String email;

    private String subject;

    private String message;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
