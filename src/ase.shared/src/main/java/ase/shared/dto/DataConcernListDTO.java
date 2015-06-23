package ase.shared.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 20.06.2015.
 */
public class DataConcernListDTO {

    private String reportId;

    private List<DataConcernDTO> dataConcerns;

    public DataConcernListDTO() {
        dataConcerns = new LinkedList<>();
    }

    public List<DataConcernDTO> getDataConcerns() {
        return dataConcerns;
    }

    public void setDataConcerns(List<DataConcernDTO> dataConcerns) {
        this.dataConcerns = dataConcerns;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
}
