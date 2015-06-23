package ase.shared.commands.reportstorage;

import ase.shared.commands.GenericHATEOSGetCommand;
import ase.shared.commands.resttypes.ReportMetadataDTORestWrapper;
import ase.shared.dto.ReportMetadataDTO;
import com.google.common.collect.ImmutableMap;
import org.springframework.hateoas.client.Traverson;

/**
 * Created by Michael on 23.06.2015.
 */
public class GetReportMetadataByReportIdCommand extends GenericHATEOSGetCommand<ReportMetadataDTORestWrapper, ReportMetadataDTO> {

    private String reportId;

    public GetReportMetadataByReportIdCommand(String url, String reportId) {
        super(ReportMetadataDTORestWrapper.class, url);
        this.reportId = reportId;
    }

    @Override
    protected Traverson.TraversalBuilder initBuilder(Traverson traverson) {
        return traverson.follow("storedReportMetadatas", "search", "findByReportId").withTemplateParameters(ImmutableMap.of("reportId", reportId));
    }
}