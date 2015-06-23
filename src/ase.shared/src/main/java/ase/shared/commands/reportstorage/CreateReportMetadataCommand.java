package ase.shared.commands.reportstorage;

import ase.shared.commands.GenericRESTCommand;
import ase.shared.commands.GenericRESTCreateCommand;
import ase.shared.model.ReportMetadata;
import ase.shared.model.analysis.Report;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 23.06.2015.
 */
public class CreateReportMetadataCommand extends GenericRESTCreateCommand<ReportMetadata>{
    private String url;
    private ReportMetadata body;

    public CreateReportMetadataCommand(String url, ReportMetadata body) {
        this.url = url;
        this.body = body;
    }

    @Override
    protected RequestEntity<ReportMetadata> getRequest() {
        return new RequestEntity<>(body, HttpMethod.POST, URI.create(url + "/storedReportMetadatas/"));
    }
}
