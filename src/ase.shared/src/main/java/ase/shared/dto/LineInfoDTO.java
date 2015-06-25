package ase.shared.dto;

import ase.shared.model.simulation.Train;

import java.util.List;

/**
 * Created by Michael on 25.06.2015.
 */
public class LineInfoDTO {

    private int timeBetweenTrains;

    private List<Integer> trains;
    private int samplingRate;

    public int getTimeBetweenTrains() {
        return timeBetweenTrains;
    }

    public void setTimeBetweenTrains(int timeBetweenTrains) {
        this.timeBetweenTrains = timeBetweenTrains;
    }

    public List<Integer> getTrains() {
        return trains;
    }

    public void setTrains(List<Integer> trains) {
        this.trains = trains;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    public int getSamplingRate() {
        return samplingRate;
    }
}
