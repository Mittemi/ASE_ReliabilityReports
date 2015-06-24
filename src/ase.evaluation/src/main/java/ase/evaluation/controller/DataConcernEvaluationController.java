package ase.evaluation.controller;

import ase.evaluation.service.DataConcernEvaluatorBase;
import ase.evaluation.service.DataConcernEvaluatorFactory;
import ase.shared.ASEModelMapper;
import ase.shared.commands.CommandFactory;
import ase.shared.commands.UpdateResult;
import ase.shared.dto.DataConcernDTO;
import ase.shared.dto.DataConcernListDTO;
import ase.shared.dto.ReportDTO;
import ase.shared.dto.ReportMetadataDTO;
import ase.shared.enums.DataConcernType;
import ase.shared.model.DataConcern;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael on 20.06.2015.
 */
@RestController
public class DataConcernEvaluationController {

    @Autowired
    private DataConcernEvaluatorFactory factory;

    @Autowired
    private CommandFactory commandFactory;

    @Autowired
    private ASEModelMapper modelMapper;

    @RequestMapping(value = "evaluate/{reportId}", produces = "application/json")
    public DataConcernListDTO evaluateReport(@PathVariable("reportId") String reportId) {

        System.out.println("Evaluation for: " + reportId);

        DataConcernListDTO dataConcernDTO = new DataConcernListDTO();
        dataConcernDTO.setReportId(reportId);

        ReportDTO report = commandFactory.getReportByIdCommand(reportId).getSingleResult();

        if(report == null) {
            return null;
        }

        List<Pair<String, ReportMetadataDTO>> reportMetadataResults = commandFactory.getReportMetadataByReportIdCommand(reportId).execute();

        if(reportMetadataResults.size() == 0) {
            return null;
        }

        ReportMetadataDTO reportMetadataDTO = reportMetadataResults.get(0).getValue();

        // if already calculated, return them
        if(reportMetadataDTO.getDataConcerns() != null && reportMetadataDTO.getDataConcerns().size() > 0) {
            reportMetadataDTO.getDataConcerns().forEach(dataConcern -> dataConcernDTO.getDataConcerns().add(modelMapper.map(dataConcern, DataConcernDTO.class)));
            return dataConcernDTO;
        }

        // for all supported data concerns
        for (DataConcernType type : DataConcernType.values()) {

            // get the evaluator for the specific concern
            DataConcernEvaluatorBase dataConcernEvaluatorBase = factory.getEvaluator(type);

            try {
                // evaluate the report using the evaluator
                DataConcern dataConcern = dataConcernEvaluatorBase.evaluateConcern(report, reportMetadataDTO);

                // add the result
                dataConcernDTO.getDataConcerns().add(modelMapper.map(dataConcern, DataConcernDTO.class));
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        reportMetadataDTO.setDataConcerns(dataConcernDTO.getDataConcerns());

        UpdateResult updateResult = commandFactory.updateReportMetadataCommand(reportMetadataDTO, reportMetadataResults.get(0).getKey()).getResult();

        return dataConcernDTO;
    }
}

