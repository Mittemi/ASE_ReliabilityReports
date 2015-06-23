package ase.shared.dto;

import ase.shared.model.DataConcern;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 20.06.2015.
 */
public class DataConcernListDTO {

    private String reportId;

    private List<DataConcern> dataConcerns;

    public DataConcernListDTO() {
        dataConcerns = new LinkedList<DataConcern>();
    }

    public List<DataConcern> getDataConcerns() {
        return dataConcerns;
    }

    public void setDataConcerns(List<DataConcern> dataConcerns) {
        this.dataConcerns = dataConcerns;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
}
