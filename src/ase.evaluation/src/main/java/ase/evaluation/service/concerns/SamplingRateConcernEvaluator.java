package ase.evaluation.service.concerns;

import ase.evaluation.service.DataConcernEvaluatorBase;
import ase.shared.dto.ReportDTO;
import ase.shared.enums.DataConcernType;
import ase.shared.model.DataConcern;
import org.springframework.stereotype.Component;

/**
 * Created by Michael on 20.06.2015.
 */
public class SamplingRateConcernEvaluator extends DataConcernEvaluatorBase {
    @Override
    public DataConcernType getDataConcernType() {
        return DataConcernType.SamplingRate;
    }

    @Override
    protected double getConcernValue(ReportDTO reportDTO) {
        return 0;
    }
}
