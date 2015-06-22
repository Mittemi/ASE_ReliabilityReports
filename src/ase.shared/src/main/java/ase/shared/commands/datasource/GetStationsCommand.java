package ase.shared.commands.datasource;

import ase.shared.commands.GenericGetCommand;
import ase.shared.commands.resttypes.StationRestWrapper;
import ase.shared.model.simulation.Station;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;
import java.util.List;

/**
 * Created by Michael on 22.06.2015.
 */
public class GetStationsCommand extends GenericGetCommand<String, Station[]> {

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
