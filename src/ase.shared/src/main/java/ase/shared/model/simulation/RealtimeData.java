package ase.shared.model.simulation;

import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * Created by Michael on 20.06.2015.
 */
public class RealtimeData {

    private Line line;

    private Train train;

    private Station station;

    private Date estimatedArrival;

    private Date plannedArrival;

    private Date currentTime;

    private Station direction;

    private boolean error;

    private boolean trainInStation;

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Date getEstimatedArrival() {
        return estimatedArrival;
    }

    public void setEstimatedArrival(Date estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
    }

    public Date getPlannedArrival() {
        return plannedArrival;
    }

    public void setPlannedArrival(Date plannedArrival) {
        this.plannedArrival = plannedArrival;
    }

    public Station getDirection() {
        return direction;
    }

    public void setDirection(Station direction) {
        this.direction = direction;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isTrainInStation() {
        return trainInStation;
    }

    public void setTrainInStation(boolean trainInStation) {
        this.trainInStation = trainInStation;
    }
}
