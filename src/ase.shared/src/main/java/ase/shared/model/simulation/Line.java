package ase.shared.model.simulation;

/**
 * Created by Michael on 20.06.2015.
 */
public class Line {

    public Line() {
    }

    public Line(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Line " + name;
    }
}
