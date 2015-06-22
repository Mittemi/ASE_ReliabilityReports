package ase.shared.commands.datasource;

import ase.shared.commands.GenericGetCommand;
import ase.shared.commands.resttypes.StationRestWrapper;
import ase.shared.model.simulation.Station;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 22.06.2015.
 */
public class GetDirectionsCommand extends GenericGetCommand<String, Station[]> {

    private String uri;
    private String lineName;

    public GetDirectionsCommand(String uri, String lineName) {
        super(new ParameterizedTypeReference<Station[]>() {
        });
        this.uri = uri;
        this.lineName = lineName;
    }

    @Override
    protected RequestEntity<String> getRequest() {

        URI url = URI.create(uri + "/static/directions/" + lineName + "/");
        return new RequestEntity<String>(HttpMethod.GET, url);
    }
}