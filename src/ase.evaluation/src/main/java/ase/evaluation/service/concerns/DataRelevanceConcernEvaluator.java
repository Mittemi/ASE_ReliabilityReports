package ase.evaluation.service.concerns;

import ase.evaluation.service.DataConcernEvaluatorBase;
import ase.shared.dto.ReportDTO;
import ase.shared.enums.DataConcernType;
import org.springframework.stereotype.Component;

/**
 * Created by Michael on 20.06.2015.
 */
public class DataRelevanceConcernEvaluator extends DataConcernEvaluatorBase {
    @Override
    public DataConcernType getDataConcernType() {
        return DataConcernType.DataRelevance;
    }

    @Override
    protected double getConcernValue(ReportDTO reportDTO) {
        return 0;
    }
}
