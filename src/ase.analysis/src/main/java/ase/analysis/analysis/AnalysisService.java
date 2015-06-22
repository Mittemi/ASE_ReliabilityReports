package ase.analysis.analysis;

import ase.analysis.Constants;
import ase.analysis.analysis.prioritizedMessaging.MessagePriority;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedJmsTemplate;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedMessage;
import ase.analysis.analysis.prioritizedMessaging.PrioritizedMessageCreator;
import ase.shared.commands.CommandFactory;
import ase.shared.dto.AnalysisRequestDTO;
import ase.shared.dto.AnalysisResponseDTO;
import ase.shared.model.simulation.RealtimeData;
import ase.shared.model.simulation.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Michael on 21.06.2015.
 */
@Component
public class AnalysisService {

    @Autowired
    private PrioritizedJmsTemplate prioritizedJmsTemplate;

    @Autowired
    private CommandFactory commandFactory;

    public AnalysisResponseDTO queueForAnalysis(AnalysisRequestDTO analysisRequestDTO, MessagePriority messagePriority) {

        System.out.println("Analysis started...");

        AnalysisResponseDTO analysisResponseDTO = new AnalysisResponseDTO();
        analysisResponseDTO.setOk(true);

        //todo: validate request

        // queue for analysis if request is valid
        if(analysisResponseDTO.isOk()) {

            try {
                prioritizedJmsTemplate.send(new PrioritizedMessageCreator(new PrioritizedMessage<Object>(analysisRequestDTO, messagePriority)));
            } catch (JmsException e) {
                e.printStackTrace();
                analysisResponseDTO.setOk(false);
                analysisResponseDTO.setMessage("Queueing for analysis failed! Please try again");
            }
        }

        System.out.println("Analysis finished...");
        return analysisResponseDTO;
    }


    @JmsListener(destination = Constants.ANALYSIS_QUEUE_NAME)
    public void jmsAnalyseTarget(AnalysisRequestDTO analysisRequestDTO) {
        System.out.println("Analysis request received (JMS)");

        System.out.println(analysisRequestDTO.getStationFrom());

        //List<Station> station = Arrays.asList(commandFactory.getStationsCommand(analysisRequestDTO.getLine()).getResult());

        List<Station> stations = Arrays.asList(commandFactory.getStationsBetweenCommand(analysisRequestDTO.getLine(), analysisRequestDTO.getStationFrom(), analysisRequestDTO.getStationTo()).getResult());

        if(stations.size() <= 1) {
            //useless analysis (bad request)
            System.out.println("Bad request");
            return;
        }

        List<Station> directions = Arrays.asList(commandFactory.getDirectionsCommand(analysisRequestDTO.getLine()).getResult());

        //sort by position asc
        directions = directions.stream().sorted((Station stationA, Station stationB) -> Integer.compare(stationA.getPosition(), stationB.getPosition())).collect(Collectors.toList());
        Station directionLow = directions.get(0);
        Station directionHigh = directions.get(1);

        Station entryStation = stations.stream().filter(x -> x.getName().equals(analysisRequestDTO.getStationFrom())).findFirst().get();
        Station exitStation = stations.stream().filter(x -> x.getName().equals(analysisRequestDTO.getStationTo())).findFirst().get();

        Station direction = entryStation.getPosition() < exitStation.getPosition() ? directionHigh : directionLow;

        List<RealtimeData> startStationRealtime = commandFactory.getRealtimeDataCommand(analysisRequestDTO.getLine(), direction.getName(), entryStation.getNumber(),analysisRequestDTO.getFrom(), analysisRequestDTO.getTo()).toList();



        System.out.println("Analysis finished (jms)");
    }

}
