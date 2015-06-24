package ase.shared.dto;

import ase.shared.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Michael on 21.06.2015.
 */
public class AnalysisRequestDTO implements Serializable {       //used as jms message!

    private String userId;

    private String line;

    private String stationFrom;

    private String stationTo;

    private int hourStart;
    private int minuteStart;

    private int hourEnd;
    private int minuteEnd;


    @DateTimeFormat(pattern = Constants.DATE_FORMAT)
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private Date from;

    @DateTimeFormat(pattern = Constants.DATE_FORMAT)
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private Date to;

    @JsonIgnore
    private ReportMetadataDTO reportMetadata;

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

    public ReportMetadataDTO getReportMetadata() {
        return reportMetadata;
    }

    public void setReportMetadata(ReportMetadataDTO reportMetadata) {
        this.reportMetadata = reportMetadata;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getHourStart() {
        return hourStart;
    }

    public void setHourStart(int hourStart) {
        this.hourStart = hourStart;
    }

    public int getMinuteStart() {
        return minuteStart;
    }

    public void setMinuteStart(int minuteStart) {
        this.minuteStart = minuteStart;
    }

    public int getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(int hourEnd) {
        this.hourEnd = hourEnd;
    }

    public int getMinuteEnd() {
        return minuteEnd;
    }

    public void setMinuteEnd(int minuteEnd) {
        this.minuteEnd = minuteEnd;
    }
}
