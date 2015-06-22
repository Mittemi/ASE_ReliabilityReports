package ase.shared.model.simulation;

/**
 * Created by Michael on 20.06.2015.
 */
public class Train {

    private int number;

    private int tripNumber;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Train: " + number;
    }

    public int getTripNumber() {
        return tripNumber;
    }

    public void setTripNumber(int tripNumber) {
        this.tripNumber = tripNumber;
    }
}
