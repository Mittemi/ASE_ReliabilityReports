package ase.shared.commands.reportstorage;

import ase.shared.commands.GenericRESTUpdateCommand;
import ase.shared.dto.ReportMetadataDTO;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 23.06.2015.
 */
public class UpdateReportMetadataCommand extends GenericRESTUpdateCommand<ReportMetadataDTO> {
    private ReportMetadataDTO body;
    private String url;
    private String id;

    public UpdateReportMetadataCommand(String url, String id, ReportMetadataDTO body) {
        this.id = id;
        this.body = body;
        this.url = url;
    }

    @Override
    protected RequestEntity<ReportMetadataDTO> getRequest() {
        return new RequestEntity<>(body, HttpMethod.PUT, URI.create(url + "/storedReportMetadatas/" + id));
    }
}
