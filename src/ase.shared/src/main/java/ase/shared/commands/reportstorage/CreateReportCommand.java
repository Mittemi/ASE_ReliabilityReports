package ase.shared.commands.reportstorage;

import ase.shared.commands.CreateResult;
import ase.shared.commands.GenericRESTCommand;
import ase.shared.commands.GenericRESTCreateCommand;
import ase.shared.model.analysis.Report;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 23.06.2015.
 */
public class CreateReportCommand extends GenericRESTCreateCommand<Report> {

    private String url;
    private Report body;

    public CreateReportCommand(String url, Report body) {
        this.url = url;
        this.body = body;
    }

    @Override
    protected RequestEntity<Report> getRequest() {
        return new RequestEntity<>(body, HttpMethod.POST, URI.create(url + "/storedReports/"));
    }
}