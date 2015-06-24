package ase.analysis.analysis.model;


import ase.shared.model.analysis.DayAnalysisResult;
import ase.shared.model.analysis.TripAnalysisResult;
import ase.shared.model.analysis.TripsAnalysisResult;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.base.AbstractDateTime;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

/**
 * Created by Michael on 23.06.2015.
 */
public class TripsAnalysis extends TripsAnalysisResult<DayAnalysis, TripAnalysis> {

    public void addDay(DayAnalysis dayAnalysis){
        getDayAnalysisResults().add(dayAnalysis);
    }

    public void analyze() {

//        if(getTripAnalysisResults().size() == 0) {
//            throw new IllegalArgumentException("Can't analyze due to missing TripAnalysisResults");
//        }

        if(getTripAnalysisResults().size() > 0) {
            setCountAnalyzedRT(getTripAnalysisResults().size());
            for (DayAnalysis trip : getDayAnalysisResults()) {
                trip.analyze();
            }

            IntSummaryStatistics entryStats = getTripAnalysisResults().stream().map(TripAnalysisResult::getEntryStayTime).mapToInt(x -> x).summaryStatistics();

            setMinimumStayTime(toTime(entryStats.getMin()));
            setMaximumStayTime(toTime(entryStats.getMax()));
            setAverageStayTime(toTime((long) Math.floor(entryStats.getAverage())));

            IntSummaryStatistics tripStats = getTripAnalysisResults().stream().map(TripAnalysisResult::getTripDuration).mapToInt(x -> x).summaryStatistics();
            setMinimumTripTime(toTime(tripStats.getMin()));
            setMaximumTripTime(toTime(tripStats.getMax()));
            setAverageTripTime(toTime((long) Math.floor(tripStats.getAverage())));

            List<DateTime> departureTimes = getDayAnalysisResults().stream().map(DayAnalysis::getLatestDeparture).collect(Collectors.toList());

            // latest possible departure from entry station (calculated in seconds --> transformed to string time)
            LongSummaryStatistics longSummaryStatistics = departureTimes.stream().mapToLong(AbstractDateTime::getSecondOfDay).summaryStatistics();

            setLatestAverageDeparture(toTime((long) Math.floor(longSummaryStatistics.getAverage())));
            setLatestMaxDeparture(toTime(longSummaryStatistics.getMax()));
            setLatestMinDeparture(toTime(longSummaryStatistics.getMin()));
        }
    }

    private String toTime(long seconds) {
        long hour = seconds/3600;
        long minute = (seconds - hour * 3600) / 60;
        long second = (seconds - hour * 3600 - minute * 60);
        String result = String.format("%02d:%02d:%02d", hour, minute, second);
        return result;
    }
}
