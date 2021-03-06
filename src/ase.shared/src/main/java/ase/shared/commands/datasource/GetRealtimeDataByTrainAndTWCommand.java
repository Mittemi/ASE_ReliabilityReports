package ase.shared.commands.datasource;

import ase.shared.Constants;
import ase.shared.commands.GenericHATEOSGetCommand;
import ase.shared.commands.resttypes.RealtimeDataWrapper;
import ase.shared.model.simulation.RealtimeData;
import com.google.common.collect.ImmutableMap;
import org.springframework.hateoas.client.Traverson;

import java.util.Date;
import java.util.Map;

/**
 * Created by Michael on 22.06.2015.
 */
public class GetRealtimeDataByTrainAndTWCommand extends GenericHATEOSGetCommand<RealtimeDataWrapper, RealtimeData> {

    private final String lineName;
    private final String direction;
    private final int trainNumber;
    private final Date from;
    private final Date to;

    public GetRealtimeDataByTrainAndTWCommand(String uri, String lineName, String direction, int trainNumber, Date from, Date to) {
        super(RealtimeDataWrapper.class, uri);
        this.lineName = lineName;
        this.direction = direction;
        this.trainNumber = trainNumber;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Traverson.TraversalBuilder initBuilder(Traverson traverson) {
        Map<String, Object> params = ImmutableMap.of(
                "lineName", lineName,
                "direction", direction,
                "trainNumber", trainNumber,
                "tFrom", Constants.SIMPLE_DATE_FORMAT_THREAD_LOCAL.get().format(from),
                "tTo", Constants.SIMPLE_DATE_FORMAT_THREAD_LOCAL.get().format(to));
        //http://localhost:9999/storedRealtimeDatas/search/findByStationAndTW?lineName=U1&direction=Leopoldau&sNumber=1&tFrom=2015-06-01T08:59:00.000%2B0000&tTo=2015-06-01T21:59:00.000%2B0000
        return traverson.follow("storedRealtimeDatas", "search", "findByTrainAndTW").withTemplateParameters(params);
    }
}
