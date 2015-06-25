package ase.shared.model.analysis;

import ase.shared.Constants;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Michael on 20.06.2015.
 */
public class ReportTimeSpan {

    public ReportTimeSpan() {
    }

    public ReportTimeSpan(Date fromDate, Date toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @DateTimeFormat(pattern = Constants.DATE_FORMAT)
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private Date fromDate;

    @DateTimeFormat(pattern = Constants.DATE_FORMAT)
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private Date toDate;

    private int hourFrom;

    private int hourTo;

    private int minuteFrom;

    private int minuteTo;

    public int getHourFrom() {
        return hourFrom;
    }

    public void setHourFrom(int hourFrom) {
        this.hourFrom = hourFrom;
    }

    public int getHourTo() {
        return hourTo;
    }

    public void setHourTo(int hourTo) {
        this.hourTo = hourTo;
    }

    public int getMinuteFrom() {
        return minuteFrom;
    }

    public void setMinuteFrom(int minuteFrom) {
        this.minuteFrom = minuteFrom;
    }

    public int getMinuteTo() {
        return minuteTo;
    }

    public void setMinuteTo(int minuteTo) {
        this.minuteTo = minuteTo;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
