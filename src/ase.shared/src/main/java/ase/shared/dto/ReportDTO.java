package ase.shared.dto;

import ase.shared.model.simulation.Line;
import ase.shared.model.ReportTimeSpan;
import ase.shared.model.simulation.Station;
import ase.shared.model.Statistics;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 20.06.2015.
 */
public class ReportDTO {

    public ReportDTO() {
        stations = new LinkedList<>();
        lines = new LinkedList<>();
    }

    private String id;

    private ReportTimeSpan time;

    private List<Line> lines;

    private List<Station> stations;

    private Statistics statistics;

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

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
