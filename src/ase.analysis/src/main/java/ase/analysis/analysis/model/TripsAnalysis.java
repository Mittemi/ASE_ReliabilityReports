package ase.analysis.analysis.model;


import ase.shared.model.analysis.TripAnalysisResult;
import ase.shared.model.analysis.TripsAnalysisResult;
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
public class TripsAnalysis extends TripsAnalysisResult<TripAnalysis> {

    public void analyze() {

        if(getTripAnalysisResults().size() == 0) {
            throw new IllegalArgumentException("Can't analyze due to missing TripAnalysisResults");
        }

        setCountAnalyzedRT(getTripAnalysisResults().size());
        for (TripAnalysis trip : getTripAnalysisResults()) {
            trip.analyze();
        }

        IntSummaryStatistics entryStats = getTripAnalysisResults().stream().map(x -> x.getEntryStayTime().getSeconds()).mapToInt(x -> x).summaryStatistics();

        setMinimumStayTime(entryStats.getMin());
        setMaximumStayTime(entryStats.getMax());
        setAverageStayTime(entryStats.getAverage());

        IntSummaryStatistics tripStats = getTripAnalysisResults().stream().map(x -> x.getTripDuration().getSeconds()).mapToInt(x -> x).summaryStatistics();
        setMinimumTripTime(tripStats.getMin());
        setMaximumTripTime(entryStats.getMax());
        setAverageTripTime(entryStats.getAverage());

        List<DateTime> departureTimes = getTripAnalysisResults().stream().map(tripAnalysisResult -> tripAnalysisResult.getEntryDeparture()).collect(Collectors.toList());
        DateTime dateTime = departureTimes.get(0);

        LongSummaryStatistics longSummaryStatistics = departureTimes.stream().mapToLong(x -> x.getMillis()).summaryStatistics();

        setLatestAverageDeparture(new DateTime((long)Math.floor(longSummaryStatistics.getAverage()), dateTime.getZone()).toDate());
        setLatestMaxDeparture(new DateTime(longSummaryStatistics.getMax(), dateTime.getZone()).toDate());
        setLatestMinDeparture(new DateTime(longSummaryStatistics.getMin(), dateTime.getZone()).toDate());
    }
}
