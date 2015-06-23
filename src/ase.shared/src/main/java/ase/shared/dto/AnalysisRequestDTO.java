package ase.shared.dto;

import ase.shared.Constants;
import ase.shared.model.ReportMetadata;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Michael on 21.06.2015.
 */
public class AnalysisRequestDTO implements Serializable {       //used as jms message!

    private String line;

    private String stationFrom;

    private String stationTo;

    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private Date from;

    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private Date to;

    @JsonIgnore
    private ReportMetadata reportMetadata;

    public AnalysisRequestDTO() {
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getStationFrom() {
        return stationFrom;
    }

    public void setStationFrom(String stationFrom) {
        this.stationFrom = stationFrom;
    }

    public String getStationTo() {
        return stationTo;
    }

    public void setStationTo(String stationTo) {
        this.stationTo = stationTo;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public ReportMetadata getReportMetadata() {
        return reportMetadata;
    }

    public void setReportMetadata(ReportMetadata reportMetadata) {
        this.reportMetadata = reportMetadata;
    }
}
