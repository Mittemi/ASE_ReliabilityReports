package ase.shared.commands.resttypes;

import ase.shared.dto.DataConcernDTO;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 23.06.2015.
 */
public class ReportMetadataRestType extends ResourceSupport implements Serializable {

    public ReportMetadataRestType() {
        dataConcerns = new LinkedList<>();
    }

    private String reportId;

    private String userId;

    private Integer priority;

    private Date requestedAt;

    private Date createdAt;

    private List<DataConcernDTO> dataConcerns;

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

    public List<DataConcernDTO> getDataConcerns() {
        return dataConcerns;
    }

    public void setDataConcerns(List<DataConcernDTO> dataConcerns) {
        this.dataConcerns = dataConcerns;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
