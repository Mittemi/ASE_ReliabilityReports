package ase.shared.model.simulation;

/**
 * Created by Michael on 20.06.2015.
 */
public class Station {

    private String name;

    private int number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Station: " + name + "(" + number + ")";
    }
}
