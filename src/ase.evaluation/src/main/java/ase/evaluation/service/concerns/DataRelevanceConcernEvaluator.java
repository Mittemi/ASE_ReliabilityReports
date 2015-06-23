package ase.evaluation.service.concerns;

import ase.evaluation.service.DataConcernEvaluatorBase;
import ase.shared.model.analysis.Report;
import ase.shared.enums.DataConcernType;

/**
 * Created by Michael on 20.06.2015.
 */
public class DataRelevanceConcernEvaluator extends DataConcernEvaluatorBase {
    @Override
    public DataConcernType getDataConcernType() {
        return DataConcernType.DataRelevance;
    }

    @Override
    protected double getConcernValue(Report report) {
        return 0;
    }
}
