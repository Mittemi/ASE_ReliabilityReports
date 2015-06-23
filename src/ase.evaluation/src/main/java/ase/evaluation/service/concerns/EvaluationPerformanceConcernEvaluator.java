package ase.evaluation.service.concerns;

import ase.evaluation.service.DataConcernEvaluatorBase;
import ase.shared.dto.ReportDTO;
import ase.shared.dto.ReportMetadataDTO;
import ase.shared.enums.DataConcernType;
import ase.shared.model.analysis.Report;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

/**
 * Created by Michael on 23.06.2015.
 *
 * Evaluates the time required for report creation:
 *  - if this value is below a certain threshold we should consider spawning new analysis instances to improve performance
 *  - if this value is above a certain threshold we should consider shutting down some analysis instances to reduce costs
 *
 *  Range: ]0->infinite[
 */
public class EvaluationPerformanceConcernEvaluator extends DataConcernEvaluatorBase {
    @Override
    public DataConcernType getDataConcernType() {
        return DataConcernType.EvaluationPerformance;
    }

    @Override
    protected double getConcernValue(ReportDTO reportDTO, ReportMetadataDTO reportMetadataDTO) {

        double seconds = Seconds.secondsBetween(new DateTime(reportMetadataDTO.getRequestedAt()), new DateTime(reportMetadataDTO.getCreatedAt())).getSeconds();
        return (double)reportMetadataDTO.getPriority() / seconds;
    }
}