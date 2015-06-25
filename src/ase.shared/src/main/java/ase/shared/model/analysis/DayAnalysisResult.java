package ase.shared.model.analysis;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 24.06.2015.
 */
public class DayAnalysisResult<TModel extends TripAnalysisResult> {

    private List<TModel> tripAnalysisResultList;

    public DayAnalysisResult() {
        this.tripAnalysisResultList = new LinkedList<>();
    }


    public List<TModel> getTripAnalysisResultList() {
        return tripAnalysisResultList;
    }

    public void setTripAnalysisResultList(List<TModel> tripAnalysisResultList) {
        this.tripAnalysisResultList = tripAnalysisResultList;
    }
}
