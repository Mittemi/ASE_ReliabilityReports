package ase.analysis.analysis.model;

import ase.shared.model.analysis.DayAnalysisResult;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Michael on 24.06.2015.
 */
public class DayAnalysis extends DayAnalysisResult<TripAnalysis> {

    public DayAnalysis(List<TripAnalysis> tripAnalysisList) {
        setTripAnalysisResultList(tripAnalysisList);
    }

    public void analyze() {
        List<TripAnalysis> tripAnalysisResultList = getTripAnalysisResultList();
        if(tripAnalysisResultList.size() > 0) {
            for (TripAnalysis tripAnalysis : tripAnalysisResultList) {
                tripAnalysis.analyze();
            }

            // sort
            tripAnalysisResultList.sort((o1, o2) -> o1.getLatestDeparture().compareTo(o2.getLatestDeparture()));
            latestDeparture = new DateTime(tripAnalysisResultList.get(tripAnalysisResultList.size() - 1).getLatestDeparture());
        }
    }

    private DateTime latestDeparture;

    public DateTime getLatestDeparture() {
        return latestDeparture;
    }

    public void setLatestDeparture(DateTime latestDeparture) {
        this.latestDeparture = latestDeparture;
    }
}
