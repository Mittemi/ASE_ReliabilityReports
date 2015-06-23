package ase.shared.model.analysis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Michael on 23.06.2015.
 */
public class TripsAnalysisResult {

    private int countAnalyzedRT;
    private Integer minimumStayTime;
    private Integer maximumStayTime;
    private double averageStayTime;

    private Date latestMinDeparture;
    private Date latestAverageDeparture;
    private Date latestMaxDeparture;
    private int minimumTripTime;
    private double averageTripTime;
    private int maximumTripTime;

    public Integer getMinimumStayTime() {
        return minimumStayTime;
    }

    public void setMinimumStayTime(Integer minimumStayTime) {
        this.minimumStayTime = minimumStayTime;
    }

    public double getAverageStayTime() {
        return averageStayTime;
    }

    public void setAverageStayTime(double averageStayTime) {
        this.averageStayTime = averageStayTime;
    }

    public Integer getMaximumStayTime() {
        return maximumStayTime;
    }

    public void setMaximumStayTime(Integer maximumStayTime) {
        this.maximumStayTime = maximumStayTime;
    }

    public int getCountAnalyzedRT() {
        return countAnalyzedRT;
    }

    public void setCountAnalyzedRT(int countAnalyzedRT) {
        this.countAnalyzedRT = countAnalyzedRT;
    }

    public int getMinimumTripTime() {
        return minimumTripTime;
    }

    public void setMinimumTripTime(int minimumTripTime) {
        this.minimumTripTime = minimumTripTime;
    }

    public Date getLatestMaxDeparture() {
        return latestMaxDeparture;
    }

    public void setLatestMaxDeparture(Date latestMaxDeparture) {
        this.latestMaxDeparture = latestMaxDeparture;
    }

    public Date getLatestAverageDeparture() {
        return latestAverageDeparture;
    }

    public void setLatestAverageDeparture(Date latestAverageDeparture) {
        this.latestAverageDeparture = latestAverageDeparture;
    }

    public Date getLatestMinDeparture() {
        return latestMinDeparture;
    }

    public void setLatestMinDeparture(Date latestMinDeparture) {
        this.latestMinDeparture = latestMinDeparture;
    }

    public double getAverageTripTime() {
        return averageTripTime;
    }

    public void setAverageTripTime(double averageTripTime) {
        this.averageTripTime = averageTripTime;
    }

    public int getMaximumTripTime() {
        return maximumTripTime;
    }

    public void setMaximumTripTime(int maximumTripTime) {
        this.maximumTripTime = maximumTripTime;
    }
}
