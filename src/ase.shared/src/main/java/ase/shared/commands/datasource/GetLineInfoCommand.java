package ase.shared.commands.datasource;

import ase.shared.commands.GenericRESTCommand;
import ase.shared.dto.LineInfoDTO;
import ase.shared.model.simulation.Line;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 25.06.2015.
 */
public class GetLineInfoCommand extends GenericRESTCommand<String, LineInfoDTO> {

    private String uri;
    private String line;

    public GetLineInfoCommand(String uri, String line) {
        super(new ParameterizedTypeReference<LineInfoDTO>() {
        });
        this.uri = uri;
        this.line = line;
    }

    @Override
    protected RequestEntity<String> getRequest() {
        return new RequestEntity<String>(HttpMethod.GET, URI.create(uri + "/static/lineinfo/" + line + "/"));
    }
}