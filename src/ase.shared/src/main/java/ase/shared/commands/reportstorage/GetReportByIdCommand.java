package ase.shared.commands.reportstorage;

import ase.shared.commands.GenericHATEOSGetCommand;
import ase.shared.model.analysis.Report;
import ase.shared.commands.resttypes.ReportRestWrapper;
import org.springframework.hateoas.client.Traverson;

/**
 * Created by Michael on 20.06.2015.
 */
public class GetReportByIdCommand extends GenericHATEOSGetCommand<ReportRestWrapper, Report> {

    private String id;

    public GetReportByIdCommand(String url, String id) {
        super(ReportRestWrapper.class, url);
        this.id = id;
    }

    @Override
    protected Traverson.TraversalBuilder initBuilder(Traverson traverson) {
        return traverson.follow("storedReports", "search", "findById", id);
    }
}
