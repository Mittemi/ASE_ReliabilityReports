package ase.shared.commands.reportstorage;

import ase.shared.commands.GenericHATEOSGetCommand;
import ase.shared.commands.resttypes.ReportDTORestWrapper;
import ase.shared.dto.ReportDTO;
import com.google.common.collect.ImmutableMap;
import org.springframework.hateoas.client.Traverson;

/**
 * Created by Michael on 20.06.2015.
 */
public class GetReportByIdCommand extends GenericHATEOSGetCommand<ReportDTORestWrapper, ReportDTO> {

    private String id;

    public GetReportByIdCommand(String url, String id) {
        super(ReportDTORestWrapper.class, url);
        this.id = id;
    }

    @Override
    protected Traverson.TraversalBuilder initBuilder(Traverson traverson) {
        return traverson.follow("storedReports", "search", "findById").withTemplateParameters(ImmutableMap.of("id", id));
    }
}
