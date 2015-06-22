package ase.shared.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 22.06.2015.
 */
public class ReportMetadata {

    private String reportId;

    private Integer priority;

    private Date requestedAt;

    private Date createdAt;

    private List<DataConcern> dataConcerns;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Date requestedAt) {
        this.requestedAt = requestedAt;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<DataConcern> getDataConcerns() {
        return dataConcerns;
    }

    public void setDataConcerns(List<DataConcern> dataConcerns) {
        this.dataConcerns = dataConcerns;
    }
}
