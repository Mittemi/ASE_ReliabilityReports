package ase.shared.model.analysis;

import ase.shared.model.simulation.Line;
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

    private ReportTimeSpan time;

    private List<Line> lines;

    private List<Station> stations;

    private TripsAnalysisResult tripsAnalysisResult;

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

    public TripsAnalysisResult getTripsAnalysisResult() {
        return tripsAnalysisResult;
    }

    public void setTripsAnalysisResult(TripsAnalysisResult tripsAnalysisResult) {
        this.tripsAnalysisResult = tripsAnalysisResult;
    }
}
