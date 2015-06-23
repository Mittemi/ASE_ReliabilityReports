package ase.shared.model.analysis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

/**
 * Created by Michael on 23.06.2015.
 */
public class TripsAnalysis {

    @JsonIgnore
    private final List<TripAnalysisResult> trips;

    private int countAnalyzedRT;
    private Integer minimumStayTime;
    private Integer maximumStayTime;
    private double averageStayTime;

    private DateTime latestMinDeparture;
    private DateTime latestAverageDeparture;
    private DateTime latestMaxDeparture;
    private int minimumTripTime;
    private double averageTripTime;
    private int maximumTripTime;

    public TripsAnalysis() {
        trips = new LinkedList<>();
    }

    public void addTrip(TripAnalysisResult tripAnalysisResult) {
        trips.add(tripAnalysisResult);
    }

    public void analyze() {

        if(trips.size() == 0) {
            throw new IllegalArgumentException("Can't analyze due to missing TripAnalysisResults");
        }

        this.countAnalyzedRT = trips.size();
        for (TripAnalysisResult trip : trips) {
            trip.analyze();
        }

        IntSummaryStatistics entryStats = trips.stream().map(x -> x.getEntryStayTime().getSeconds()).mapToInt(x -> x).summaryStatistics();

        this.minimumStayTime = entryStats.getMin();
        this.maximumStayTime = entryStats.getMax();
        this.averageStayTime = entryStats.getAverage();

        IntSummaryStatistics tripStats = trips.stream().map(x -> x.getTripDuration().getSeconds()).mapToInt(x -> x).summaryStatistics();
        this.minimumTripTime = tripStats.getMin();
        this.maximumTripTime = entryStats.getMax();
        this.averageTripTime = entryStats.getAverage();

        List<DateTime> departureTimes = trips.stream().map(tripAnalysisResult -> tripAnalysisResult.getEntryDeparture()).collect(Collectors.toList());
        DateTime dateTime = departureTimes.get(0);

        LongSummaryStatistics longSummaryStatistics = departureTimes.stream().mapToLong(x -> x.getMillis()).summaryStatistics();

        this.latestAverageDeparture = new DateTime((long)Math.floor(longSummaryStatistics.getAverage()), dateTime.getZone());
        this.latestMaxDeparture = new DateTime(longSummaryStatistics.getMax(), dateTime.getZone());
        this.latestMinDeparture = new DateTime(longSummaryStatistics.getMin(), dateTime.getZone());
    }

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

    public DateTime getLatestMaxDeparture() {
        return latestMaxDeparture;
    }

    public void setLatestMaxDeparture(DateTime latestMaxDeparture) {
        this.latestMaxDeparture = latestMaxDeparture;
    }

    public DateTime getLatestAverageDeparture() {
        return latestAverageDeparture;
    }

    public void setLatestAverageDeparture(DateTime latestAverageDeparture) {
        this.latestAverageDeparture = latestAverageDeparture;
    }

    public DateTime getLatestMinDeparture() {
        return latestMinDeparture;
    }

    public void setLatestMinDeparture(DateTime latestMinDeparture) {
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