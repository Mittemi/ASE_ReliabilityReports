package ase.evaluation.service;

import ase.shared.commands.CommandFactory;
import ase.shared.dto.ReportDTO;
import ase.shared.enums.DataConcernType;
import ase.shared.model.DataConcern;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Michael on 20.06.2015.
 */
public abstract class DataConcernEvaluatorBase {

    @Autowired
    private CommandFactory commandFactory;

    public abstract DataConcernType getDataConcernType();

    public DataConcern evaluateConcern(ReportDTO reportDTO) {
        DataConcern dataConcern = new DataConcern();
        dataConcern.setConcernType(getDataConcernType());
        dataConcern.setValue(getConcernValue(reportDTO));
        return dataConcern;
    }

    protected abstract double getConcernValue(ReportDTO reportDTO);

    protected CommandFactory getCommandFactory() {
        return commandFactory;
    }
}
