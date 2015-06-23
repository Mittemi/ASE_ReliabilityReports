package ase.shared.commands.datasource;

import ase.shared.commands.GenericRESTCommand;
import ase.shared.model.simulation.Station;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 22.06.2015.
 */
public class GetStationsCommand extends GenericRESTCommand<String, Station[]> {

    private String lineName;

    private String uri;

    public GetStationsCommand(String uri, String lineName) {
        super(new ParameterizedTypeReference<Station[]>() { });

        this.uri = uri;
        this.lineName = lineName;
    }

    @Override
    protected RequestEntity<String> getRequest() {
        return new RequestEntity<>(HttpMethod.GET, URI.create(uri + "/static/stations/" + lineName + "/"));
    }
}
