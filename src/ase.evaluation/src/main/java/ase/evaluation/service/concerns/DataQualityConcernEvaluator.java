package ase.evaluation.service.concerns;

import ase.evaluation.service.DataConcernEvaluatorBase;
import ase.shared.dto.ReportDTO;
import ase.shared.dto.ReportMetadataDTO;
import ase.shared.enums.DataConcernType;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;

/**
 * Created by Michael on 25.06.2015.
 *
 * Data Concern: Data Quality
 *
 * Evaluates the quality of the report. In a nutshell: a report is of higher quality if more days are considered during generation.
 *  - impacts the price
 *
 *  -1 if concern can't be applied, due a request misconfiguration (e.g. no days to process)
 */
public class DataQualityConcernEvaluator extends DataConcernEvaluatorBase {

    @Override
    public DataConcernType getDataConcernType() {
        return DataConcernType.DataQuality;
    }

    @Override
    protected double getConcernValue(ReportDTO reportDTO, ReportMetadataDTO reportMetadataDTO) {

        int days = Days.daysBetween(new DateTime(reportDTO.getTime().getFromDate()), new DateTime(reportDTO.getTime().getToDate())).getDays();

        // we only care about the time component
        DateTime timeFrameStart = new DateTime(2015, 1, 1, reportDTO.getTime().getHourFrom(), reportDTO.getTime().getMinuteFrom());
        DateTime timeFrameEnd = new DateTime(2015, 1, 1, reportDTO.getTime().getHourTo(), reportDTO.getTime().getMinuteTo());
        int frameDurationMinutes = Minutes.minutesBetween(timeFrameStart, timeFrameEnd).getMinutes();

        if(days == 0)
            return -1;

        return (reportDTO.getTripsAnalysisResult().getCountAnalyzedRT() * 10 / frameDurationMinutes) * days;
    }

}
