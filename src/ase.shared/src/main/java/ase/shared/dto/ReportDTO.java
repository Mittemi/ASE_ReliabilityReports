package ase.shared.dto;

import ase.shared.model.analysis.Report;

/**
 * Created by Michael on 23.06.2015.
 */
public class ReportDTO extends Report {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
