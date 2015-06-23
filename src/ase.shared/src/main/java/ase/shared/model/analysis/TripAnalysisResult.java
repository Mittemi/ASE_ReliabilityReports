package ase.shared.model.analysis;

import ase.shared.model.simulation.RealtimeData;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.List;

/**
 * Created by Michael on 23.06.2015.
 */
public class TripAnalysisResult {

    private Integer tripNumber;

    private DateTime entryArrival;
    private DateTime entryDeparture;

    private DateTime exitArrival;

    private List<RealtimeData> entryRT;

    private List<RealtimeData> exitRT;
    private Seconds tripDuration;
    private Seconds entryStayTime;
    private DateTime entryPlannedArrival;
    private DateTime exitPlannedArrival;
    private Seconds plannedTripDuration;

    public TripAnalysisResult(Integer tripNumber) {
        this.tripNumber = tripNumber;
    }

    /**
     * calculates several values for this one trip
     */
    public void analyze() {
        // get arrival date in entry station, exit date might be later if the train stayed in the station for some time
        this.entryArrival = new DateTime(entryRT.get(0).getCurrentTime());
        this.entryDeparture = new DateTime(entryRT.get(entryRT.size() - 1).getCurrentTime());      //obviously the next minute!! though might result in a timespan of 0 min due to sample data!

        this.exitArrival = new DateTime(exitRT.get(0).getCurrentTime());

        this.entryPlannedArrival = new DateTime(entryRT.get(0).getPlannedArrival());
        this.exitPlannedArrival = new DateTime(exitRT.get(0).getPlannedArrival());
        this.plannedTripDuration = Seconds.secondsBetween(entryPlannedArrival, exitPlannedArrival);

        this.entryStayTime = Seconds.secondsBetween(entryArrival, entryDeparture);
        this.tripDuration = Seconds.secondsBetween(entryDeparture, exitArrival);
    }

    public Integer getTripNumber() {
        return tripNumber;
    }

    public void setTripNumber(Integer tripNumber) {
        this.tripNumber = tripNumber;
    }

    public DateTime getEntryArrival() {
        return entryArrival;
    }

    public void setEntryArrival(DateTime entryArrival) {
        this.entryArrival = entryArrival;
    }

    public DateTime getEntryDeparture() {
        return entryDeparture;
    }

    public void setEntryDeparture(DateTime entryDeparture) {
        this.entryDeparture = entryDeparture;
    }

    public DateTime getExitArrival() {
        return exitArrival;
    }

    public void setExitArrival(DateTime exitArrival) {
        this.exitArrival = exitArrival;
    }

    public List<RealtimeData> getEntryRT() {
        return entryRT;
    }

    public void setEntryRT(List<RealtimeData> entryRT) {
        this.entryRT = entryRT;
    }

    public List<RealtimeData> getExitRT() {
        return exitRT;
    }

    public void setExitRT(List<RealtimeData> exitRT) {
        this.exitRT = exitRT;
    }

    public Seconds getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(Seconds tripDuration) {
        this.tripDuration = tripDuration;
    }

    public Seconds getEntryStayTime() {
        return entryStayTime;
    }

    public void setEntryStayTime(Seconds entryStayTime) {
        this.entryStayTime = entryStayTime;
    }

    public DateTime getEntryPlannedArrival() {
        return entryPlannedArrival;
    }

    public void setEntryPlannedArrival(DateTime entryPlannedArrival) {
        this.entryPlannedArrival = entryPlannedArrival;
    }

    public DateTime getExitPlannedArrival() {
        return exitPlannedArrival;
    }

    public void setExitPlannedArrival(DateTime exitPlannedArrival) {
        this.exitPlannedArrival = exitPlannedArrival;
    }

    public Seconds getPlannedTripDuration() {
        return plannedTripDuration;
    }

    public void setPlannedTripDuration(Seconds plannedTripDuration) {
        this.plannedTripDuration = plannedTripDuration;
    }
}
