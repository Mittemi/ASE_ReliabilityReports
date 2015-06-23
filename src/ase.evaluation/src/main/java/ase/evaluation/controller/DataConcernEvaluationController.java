package ase.evaluation.controller;

import ase.evaluation.service.DataConcernEvaluatorBase;
import ase.evaluation.service.DataConcernEvaluatorFactory;
import ase.shared.commands.CommandFactory;
import ase.shared.dto.DataConcernDTO;
import ase.shared.model.analysis.Report;
import ase.shared.enums.DataConcernType;
import ase.shared.model.DataConcern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael on 20.06.2015.
 */
@RestController
@RequestMapping(value = "/")
public class DataConcernEvaluationController {

    @Autowired
    private DataConcernEvaluatorFactory factory;

    @Autowired
    private CommandFactory commandFactory;

    @RequestMapping(value = "{reportId}", produces = "application/json")
    public DataConcernDTO evaluateReport(@PathVariable String reportId) {

        DataConcernDTO dataConcernDTO = new DataConcernDTO();
        dataConcernDTO.setReportId(reportId);

        Report report = commandFactory.getReportByIdCommand(reportId).getSingleResult();

        // for all supported data concerns
        for (DataConcernType type : DataConcernType.values()) {

            // get the evaluator for the specific concern
            DataConcernEvaluatorBase dataConcernEvaluatorBase = factory.getEvaluator(type);

            // evaluate the report using the evaluator
            DataConcern dataConcern = dataConcernEvaluatorBase.evaluateConcern(report);

            // add the result
            dataConcernDTO.getDataConcerns().add(dataConcern);
        }
        return dataConcernDTO;
    }
}

