package ase.shared.commands.reportstorage;

import ase.shared.commands.GenericHATEOSGetCommand;
import ase.shared.commands.resttypes.ReportMetadataDTORestWrapper;
import ase.shared.dto.ReportMetadataDTO;
import com.google.common.collect.ImmutableMap;
import org.springframework.hateoas.client.Traverson;

/**
 * Created by Michael on 23.06.2015.
 */
public class GetReportMetadataByIdCommand extends GenericHATEOSGetCommand<ReportMetadataDTORestWrapper, ReportMetadataDTO> {

    private String id;

    public GetReportMetadataByIdCommand(String url, String id) {
        super(ReportMetadataDTORestWrapper.class, url);
        this.id = id;
    }

    @Override
    protected Traverson.TraversalBuilder initBuilder(Traverson traverson) {
        return traverson.follow("storedReportMetadatas", "search", "findById").withTemplateParameters(ImmutableMap.of("id", id));
    }
}