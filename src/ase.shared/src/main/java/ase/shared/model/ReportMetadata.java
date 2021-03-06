package ase.shared.model;

import ase.shared.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 22.06.2015.
 */
public class ReportMetadata<TDataConcern extends DataConcern> implements Serializable {

    public ReportMetadata() {
        dataConcerns = new LinkedList<>();
    }

    private String reportId;

    private String userId;

    private Integer priority;

    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    private Date requestedAt;

    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    private Date createdAt;

    private List<TDataConcern> dataConcerns;

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

    public List<TDataConcern> getDataConcerns() {
        return dataConcerns;
    }

    public void setDataConcerns(List<TDataConcern> dataConcerns) {
        this.dataConcerns = dataConcerns;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
