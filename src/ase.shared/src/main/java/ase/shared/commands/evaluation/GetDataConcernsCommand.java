package ase.shared.commands.evaluation;

import ase.shared.commands.GenericRESTCommand;
import ase.shared.dto.DataConcernDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 23.06.2015.
 */
public class GetDataConcernsCommand extends GenericRESTCommand<Object, DataConcernDTO> {
    private String reportId;
    private String url;

    public GetDataConcernsCommand(String url, String reportId) {
        super(new ParameterizedTypeReference<DataConcernDTO>() {
        });
        this.reportId = reportId;
        this.url = url;
    }

    @Override
    protected RequestEntity<Object> getRequest() {
        return new RequestEntity<Object>(HttpMethod.GET, URI.create(url + "/evaluate/" + reportId));
    }
}
