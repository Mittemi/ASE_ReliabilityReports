package ase.shared.dto;

import java.io.Serializable;

/**
 * Created by Michael on 21.06.2015.
 */
public class AnalysisResponseDTO implements Serializable {

    private boolean ok;

    private String message;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
