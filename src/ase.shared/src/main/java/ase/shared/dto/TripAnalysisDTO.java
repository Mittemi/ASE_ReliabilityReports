package ase.shared.dto;

import ase.shared.model.analysis.TripAnalysisResult;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael on 24.06.2015.
 */
public class TripAnalysisDTO {

    private int countAnalyzedRT;
    private String minimumStayTime;
    private String maximumStayTime;
    private String averageStayTime;

    private String latestMinDeparture;
    private String latestAverageDeparture;
    private String latestMaxDeparture;
    private String minimumTripTime;
    private String averageTripTime;
    private String maximumTripTime;

    public String getMinimumStayTime() {
        return minimumStayTime;
    }

    public void setMinimumStayTime(String minimumStayTime) {
        this.minimumStayTime = minimumStayTime;
    }

    public String getMaximumStayTime() {
        return maximumStayTime;
    }

    public void setMaximumStayTime(String maximumStayTime) {
        this.maximumStayTime = maximumStayTime;
    }

    public String getAverageStayTime() {
        return averageStayTime;
    }

    public void setAverageStayTime(String averageStayTime) {
        this.averageStayTime = averageStayTime;
    }

    public String getMinimumTripTime() {
        return minimumTripTime;
    }

    public void setMinimumTripTime(String minimumTripTime) {
        this.minimumTripTime = minimumTripTime;
    }

    public String getAverageTripTime() {
        return averageTripTime;
    }

    public void setAverageTripTime(String averageTripTime) {
        this.averageTripTime = averageTripTime;
    }

    public String getMaximumTripTime() {
        return maximumTripTime;
    }

    public void setMaximumTripTime(String maximumTripTime) {
        this.maximumTripTime = maximumTripTime;
    }

    public int getCountAnalyzedRT() {
        return countAnalyzedRT;
    }

    public void setCountAnalyzedRT(int countAnalyzedRT) {
        this.countAnalyzedRT = countAnalyzedRT;
    }

    public String getLatestMaxDeparture() {
        return latestMaxDeparture;
    }

    public void setLatestMaxDeparture(String latestMaxDeparture) {
        this.latestMaxDeparture = latestMaxDeparture;
    }

    public String getLatestAverageDeparture() {
        return latestAverageDeparture;
    }

    public void setLatestAverageDeparture(String latestAverageDeparture) {
        this.latestAverageDeparture = latestAverageDeparture;
    }

    public String getLatestMinDeparture() {
        return latestMinDeparture;
    }

    public void setLatestMinDeparture(String latestMinDeparture) {
        this.latestMinDeparture = latestMinDeparture;
    }
}
