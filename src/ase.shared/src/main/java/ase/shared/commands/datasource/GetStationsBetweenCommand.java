package ase.shared.commands.datasource;

import ase.shared.commands.GenericGetCommand;
import ase.shared.model.simulation.Station;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 22.06.2015.
 */
public class GetStationsBetweenCommand extends GenericGetCommand<String, Station[]> {

    private String lineName;
    private final String stationA;
    private final String stationB;

    private String uri;

    public GetStationsBetweenCommand(String uri, String lineName, String stationA, String stationB) {
        super(new ParameterizedTypeReference<Station[]>() { });

        this.uri = uri;
        this.lineName = lineName;
        this.stationA = stationA;
        this.stationB = stationB;
    }

    @Override
    protected RequestEntity<String> getRequest() {
        return new RequestEntity<>(HttpMethod.GET, URI.create(uri + "/static/stations/" + lineName + "/between/" + stationA + "/" + stationB + "/" ));
    }
    //"/stations/{line}/between/{a}/{b}/"
}
