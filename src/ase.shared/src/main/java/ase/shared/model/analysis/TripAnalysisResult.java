package ase.shared.model.analysis;

import ase.shared.model.simulation.RealtimeData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 23.06.2015.
 */
public class TripAnalysisResult {

    private Integer tripNumber;

    private Integer trainNumber;

    private Date entryArrival;
    private Date entryDeparture;

    private Date exitArrival;

    @JsonIgnore
    private List<RealtimeData> entryRT;

    @JsonIgnore
    private List<RealtimeData> exitRT;
    private int tripDuration;
    private int entryStayTime;
    private Date entryPlannedArrival;
    private Date exitPlannedArrival;
    private int plannedTripDuration;

    private Date latestDeparture;

    public TripAnalysisResult(Integer tripNumber, Integer trainNumber) {
        this.tripNumber = tripNumber;
        this.trainNumber = trainNumber;
    }

    public TripAnalysisResult() {

    }

    public Integer getTripNumber() {
        return tripNumber;
    }

    public void setTripNumber(Integer tripNumber) {
        this.tripNumber = tripNumber;
    }

    public Date getEntryArrival() {
        return entryArrival;
    }

    public void setEntryArrival(Date entryArrival) {
        this.entryArrival = entryArrival;
    }

    public Date getEntryDeparture() {
        return entryDeparture;
    }

    public void setEntryDeparture(Date entryDeparture) {
        this.entryDeparture = entryDeparture;
    }

    public Date getExitArrival() {
        return exitArrival;
    }

    public void setExitArrival(Date exitArrival) {
        this.exitArrival = exitArrival;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getEntryStayTime() {
        return entryStayTime;
    }

    public void setEntryStayTime(int entryStayTime) {
        this.entryStayTime = entryStayTime;
    }

    public Date getEntryPlannedArrival() {
        return entryPlannedArrival;
    }

    public void setEntryPlannedArrival(Date entryPlannedArrival) {
        this.entryPlannedArrival = entryPlannedArrival;
    }

    public Date getExitPlannedArrival() {
        return exitPlannedArrival;
    }

    public void setExitPlannedArrival(Date exitPlannedArrival) {
        this.exitPlannedArrival = exitPlannedArrival;
    }

    public int getPlannedTripDuration() {
        return plannedTripDuration;
    }

    public void setPlannedTripDuration(int plannedTripDuration) {
        this.plannedTripDuration = plannedTripDuration;
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


    public Date getLatestDeparture() {
        return latestDeparture;
    }

    public void setLatestDeparture(Date latestDeparture) {
        this.latestDeparture = latestDeparture;
    }

    public Integer getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(Integer trainNumber) {
        this.trainNumber = trainNumber;
    }
}
