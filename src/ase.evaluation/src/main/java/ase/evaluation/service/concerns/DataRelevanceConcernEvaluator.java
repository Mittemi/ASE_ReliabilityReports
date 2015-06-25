package ase.evaluation.service.concerns;

import ase.evaluation.service.DataConcernEvaluatorBase;
import ase.shared.commands.CommandFactory;
import ase.shared.dto.LineInfoDTO;
import ase.shared.dto.ReportDTO;
import ase.shared.dto.ReportMetadataDTO;
import ase.shared.enums.DataConcernType;
import javafx.util.Pair;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 20.06.2015.
 *
 * Data Concern: Data Relevance
 *
 * Evaluates the relevancy of the report. In a nutshell: a report concerning rush hour on a highly frequented line is more important than at e.g. midnight
 *  - impacts the price
 *
 *  -1 if concern can't be applied, due a request misconfiguration (e.g. no days to process)
 */
public class DataRelevanceConcernEvaluator extends DataConcernEvaluatorBase {

    @Autowired
    private CommandFactory commandFactory;

    @Override
    public DataConcernType getDataConcernType() {
        return DataConcernType.DataRelevance;
    }

    @Override
    protected double getConcernValue(ReportDTO reportDTO, ReportMetadataDTO reportMetadataDTO) {

        if(reportDTO.getLines() == null || reportDTO.getLines().size() == 0)
            return -1;      //useless no lines

        if(reportDTO.getStations() == null || reportDTO.getStations().size() == 0)
            return -1;      //useless no stations

        LineInfoDTO lineInfoDTO = commandFactory.getLineInfoCommand(reportDTO.getLines().get(0).getName()).getResult();

        if(lineInfoDTO == null)
            return -1;      // unknown error

        double relevancy = getRelevancy(reportDTO);
        return relevancy / lineInfoDTO.getTimeBetweenTrains();
    }

    private int getRelevancy(ReportDTO reportDTO) {
        Map<Pair<Integer, Integer>, Integer> relevancy = new HashMap<>();

        relevancy.put(new Pair<>(0, 5), 1);     //0-6 low importance
        relevancy.put(new Pair<>(6, 6), 8);   // quite important
        relevancy.put(new Pair<>(7, 9), 10);   // rush hour
        relevancy.put(new Pair<>(10, 14), 6);   // during day
        relevancy.put(new Pair<>(15, 17), 10);   // rush hour
        relevancy.put(new Pair<>(18, 20), 5);   // not so much
        relevancy.put(new Pair<>(21, 23), 3);   // night

        for (Pair<Integer, Integer> item : relevancy.keySet()) {

            if(item.getKey() <= reportDTO.getTime().getHourFrom() && item.getValue() >= reportDTO.getTime().getHourFrom()) {
                return relevancy.get(item);
            }
        }
        return 0;   //fallback, should not happen
    }
}
