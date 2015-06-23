package ase.shared.commands.reportstorage;

import ase.shared.commands.GenericHATEOSGetCommand;
import ase.shared.commands.resttypes.ReportMetadataRestWrapper;
import ase.shared.commands.resttypes.ReportRestWrapper;
import ase.shared.model.ReportMetadata;
import ase.shared.model.analysis.Report;
import org.springframework.hateoas.client.Traverson;

/**
 * Created by Michael on 23.06.2015.
 */
public class GetReportMetadataByIdCommand extends GenericHATEOSGetCommand<ReportMetadataRestWrapper, ReportMetadata> {

    private String id;

    public GetReportMetadataByIdCommand(String url, String id) {
        super(ReportMetadataRestWrapper.class, url);
        this.id = id;
    }

    @Override
    protected Traverson.TraversalBuilder initBuilder(Traverson traverson) {
        return traverson.follow("storedReportMetadatas", "search", "findById", id);
    }
}