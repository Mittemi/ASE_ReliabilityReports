package ase.evaluation.service.concerns;

import ase.evaluation.service.DataConcernEvaluatorBase;
import ase.shared.dto.ReportDTO;
import ase.shared.dto.ReportMetadataDTO;
import ase.shared.enums.DataConcernType;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Minutes;

import java.time.Period;

/**
 * Created by Michael on 20.06.2015.
 */
public class DataRelevanceConcernEvaluator extends DataConcernEvaluatorBase {
    @Override
    public DataConcernType getDataConcernType() {
        return DataConcernType.DataRelevance;
    }

    @Override
    protected double getConcernValue(ReportDTO reportDTO, ReportMetadataDTO reportMetadataDTO) {

        int days = Days.daysBetween(new DateTime(reportDTO.getTime().getFromDate()), new DateTime(reportDTO.getTime().getToDate())).getDays();

        DateTime timeFrameStart = new DateTime(2015, 1, 1, reportDTO.getTime().getHourFrom(), reportDTO.getTime().getMinuteFrom());
        DateTime timeFrameEnd = new DateTime(2015, 1, 1, reportDTO.getTime().getHourTo(), reportDTO.getTime().getMinuteTo());
        int frameDurationMinutes = Minutes.minutesBetween(timeFrameStart, timeFrameEnd).getMinutes();

        if(days == 0)
            return 0;

        //reportDTO.getTripsAnalysisResult().get

        // rt per minute in relation to timeframe for analysis
        return reportDTO.getTripsAnalysisResult().getCountAnalyzedRT() / days;
    }
}
