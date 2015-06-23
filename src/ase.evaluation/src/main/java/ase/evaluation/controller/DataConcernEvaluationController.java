package ase.evaluation.controller;

import ase.evaluation.service.DataConcernEvaluatorBase;
import ase.evaluation.service.DataConcernEvaluatorFactory;
import ase.shared.ASEModelMapper;
import ase.shared.commands.CommandFactory;
import ase.shared.dto.DataConcernDTO;
import ase.shared.dto.DataConcernListDTO;
import ase.shared.dto.ReportDTO;
import ase.shared.dto.ReportMetadataDTO;
import ase.shared.model.ReportMetadata;
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

    @Autowired
    private ASEModelMapper modelMapper;

    @RequestMapping(value = "evaluate/{reportId}", produces = "application/json")
    public DataConcernListDTO evaluateReport(@PathVariable String reportId) {

        DataConcernListDTO dataConcernDTO = new DataConcernListDTO();
        dataConcernDTO.setReportId(reportId);

        ReportDTO report = commandFactory.getReportByIdCommand(reportId).getSingleResult();
        ReportMetadataDTO reportMetadataDTO = commandFactory.getReportMetadataByIdCommand(reportId).getSingleResult();

        // for all supported data concerns
        for (DataConcernType type : DataConcernType.values()) {

            // get the evaluator for the specific concern
            DataConcernEvaluatorBase dataConcernEvaluatorBase = factory.getEvaluator(type);

            // evaluate the report using the evaluator
            DataConcern dataConcern = dataConcernEvaluatorBase.evaluateConcern(report, reportMetadataDTO);

            // add the result
            dataConcernDTO.getDataConcerns().add(modelMapper.map(dataConcern, DataConcernDTO.class));
        }
        reportMetadataDTO.setDataConcerns(dataConcernDTO.getDataConcerns());

        commandFactory.updateReportMetadataCommand(reportMetadataDTO).getResult();

        return dataConcernDTO;
    }
}

