package ase.shared.commands.reportstorage;

import ase.shared.commands.GenericRESTCommand;
import ase.shared.model.ReportMetadata;
import ase.shared.model.analysis.Report;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 23.06.2015.
 */
public class CreateReportMetadataCommand extends GenericRESTCommand<ReportMetadata, Object> {
    private String url;
    private ReportMetadata body;

    public CreateReportMetadataCommand(String url, ReportMetadata body) {
        super(new ParameterizedTypeReference<Object>() {
        });
        this.url = url;
        this.body = body;
    }

    @Override
    protected RequestEntity<ReportMetadata> getRequest() {
        return new RequestEntity<>(body, HttpMethod.POST, URI.create(url + "/storedReportMetadatas/"));
    }
}
