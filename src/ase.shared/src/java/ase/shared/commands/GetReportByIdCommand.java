package ase.shared.commands;

import ase.shared.dto.ReportDTO;
import ase.shared.commands.resttypes.ReportRestWrapper;
import org.springframework.hateoas.client.Traverson;

/**
 * Created by Michael on 20.06.2015.
 */
public class GetReportByIdCommand extends GenericGetCommand<ReportRestWrapper, ReportDTO> {

    private String id;

    public GetReportByIdCommand(String url, String id) {
        super(ReportRestWrapper.class, url);
        this.id = id;
    }

    @Override
    protected Traverson.TraversalBuilder initBuilder(Traverson traverson) {
        //TODO: check this, might need a search instead!
        return traverson.follow("reports", id);
    }
}
