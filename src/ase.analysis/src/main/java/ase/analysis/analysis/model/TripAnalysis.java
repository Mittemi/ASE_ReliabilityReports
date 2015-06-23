package ase.analysis.analysis.model;

import ase.shared.model.analysis.TripAnalysisResult;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

/**
 * Created by Michael on 23.06.2015.
 */
public class TripAnalysis extends TripAnalysisResult {
    public TripAnalysis(Integer tripNumber) {
        super(tripNumber);
    }


    /**
     * calculates several values for this one trip
     */
    public void analyze() {
        // get arrival date in entry station, exit date might be later if the train stayed in the station for some time
        setEntryArrival(new DateTime(getEntryRT().get(0).getCurrentTime()));
        setEntryDeparture(new DateTime(getEntryRT().get(getEntryRT().size()).getCurrentTime()));      //obviously the next minute!! though might result in a timespan of 0 min due to sample data)!

        setExitArrival(new DateTime(getExitRT().get(0).getCurrentTime()));

        setEntryPlannedArrival(new DateTime(getEntryRT().get(0).getPlannedArrival()));
        setExitPlannedArrival(new DateTime(getExitRT().get(0).getPlannedArrival()));
        setPlannedTripDuration(Seconds.secondsBetween(getEntryPlannedArrival(), getExitPlannedArrival()));

        setEntryStayTime(Seconds.secondsBetween(getEntryArrival(), getEntryDeparture()));
        setTripDuration(Seconds.secondsBetween(getEntryDeparture(), getExitArrival()));
    }
}
