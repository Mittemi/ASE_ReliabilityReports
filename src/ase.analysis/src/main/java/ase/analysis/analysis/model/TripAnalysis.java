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
        setEntryArrival(getEntryRT().get(0).getCurrentTime());
        setEntryDeparture(getEntryRT().get(getEntryRT().size() - 1).getCurrentTime());      //obviously the next minute!! though might result in a timespan of 0 min due to sample data)!

        setExitArrival(getExitRT().get(0).getCurrentTime());

        setEntryPlannedArrival(getEntryRT().get(0).getPlannedArrival());
        setExitPlannedArrival(getExitRT().get(0).getPlannedArrival());
        setPlannedTripDuration(Seconds.secondsBetween(new DateTime(getEntryPlannedArrival()),new DateTime(getExitPlannedArrival())).getSeconds());

        setEntryStayTime(Seconds.secondsBetween(new DateTime(getEntryArrival()), new DateTime(getEntryDeparture())).getSeconds());
        setTripDuration(Seconds.secondsBetween(new DateTime(getEntryDeparture()), new DateTime(getExitArrival())).getSeconds());

        setLatestDeparture(getEntryRT().get(getEntryRT().size() - 1).getCurrentTime());
    }
}
