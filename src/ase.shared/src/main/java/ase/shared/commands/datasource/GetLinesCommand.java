package ase.shared.commands.datasource;

import ase.shared.commands.GenericRESTCommand;
import ase.shared.model.simulation.Line;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 22.06.2015.
 */
public class GetLinesCommand extends GenericRESTCommand<String, Line[]> {

    private String uri;

    public GetLinesCommand(String uri) {
        super(new ParameterizedTypeReference<Line[]>() {
        });
        this.uri = uri;
    }

    @Override
    protected RequestEntity<String> getRequest() {
        return new RequestEntity<String>(HttpMethod.GET, URI.create(uri + "/static/lines/"));
    }
}