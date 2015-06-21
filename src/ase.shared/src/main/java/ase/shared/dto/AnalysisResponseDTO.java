package ase.shared.dto;

/**
 * Created by Michael on 21.06.2015.
 */
public class AnalysisResponseDTO {

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
