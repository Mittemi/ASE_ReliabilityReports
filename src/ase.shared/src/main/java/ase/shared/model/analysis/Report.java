package ase.shared.model.analysis;

import ase.shared.model.analysis.TripAnalysisResult;
import ase.shared.model.simulation.Line;
import ase.shared.model.analysis.ReportTimeSpan;
import ase.shared.model.simulation.Station;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 20.06.2015.
 */
public class Report {

    public Report() {
        stations = new LinkedList<>();
        lines = new LinkedList<>();
    }

    private String id;

    private ReportTimeSpan time;

    private List<Line> lines;

    private List<Station> stations;

    private TripsAnalysis tripsAnalysis;

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public ReportTimeSpan getTime() {
        return time;
    }

    public void setTime(ReportTimeSpan time) {
        this.time = time;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TripsAnalysis getTripsAnalysis() {
        return tripsAnalysis;
    }

    public void setTripsAnalysis(TripsAnalysis tripsAnalysis) {
        this.tripsAnalysis = tripsAnalysis;
    }
}
