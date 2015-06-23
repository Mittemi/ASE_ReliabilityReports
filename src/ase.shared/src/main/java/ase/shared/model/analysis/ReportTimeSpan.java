package ase.shared.model.analysis;

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

    private Date fromDate;

    private Date toDate;

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
