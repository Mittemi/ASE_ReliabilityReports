package ase.evaluation.service.concerns;

import ase.evaluation.service.DataConcernEvaluatorBase;
import ase.shared.commands.CommandFactory;
import ase.shared.dto.LineInfoDTO;
import ase.shared.dto.ReportDTO;
import ase.shared.dto.ReportMetadataDTO;
import ase.shared.enums.DataConcernType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Michael on 20.06.2015.
 *
 * Data Concern: Data quality
 *
 * Evaluates the sampling rate in relation to the frequency trains are passing the station
 *  - if the value is below a certain threshold the sampling rate should be increased at this line
 *  - if the value is above a certain threshold we should consider reducing the sampling rate
 *
 *  Range: ]0 -> infinite[
 */
public class SamplingRateConcernEvaluator extends DataConcernEvaluatorBase {

    @Autowired
    private CommandFactory commandFactory;

    @Override
    public DataConcernType getDataConcernType() {
        return DataConcernType.SamplingRate;
    }

    @Override
    protected double getConcernValue(ReportDTO reportDTO, ReportMetadataDTO reportMetadataDTO) {

        if(reportDTO.getLines() == null || reportDTO.getLines().size() == 0)
            return -1;      //useless no lines

        if(reportDTO.getStations() == null || reportDTO.getStations().size() == 0)
            return -1;      //useless no stations

        LineInfoDTO lineInfoDTO = commandFactory.getLineInfoCommand(reportDTO.getLines().get(0).getName()).getResult();


        if(lineInfoDTO == null)
            return -1;      //evaluation not possible, reason unknown, better be safe than sorry


        return (lineInfoDTO.getTimeBetweenTrains() * 60 / lineInfoDTO.getSamplingRate());
    }
}
