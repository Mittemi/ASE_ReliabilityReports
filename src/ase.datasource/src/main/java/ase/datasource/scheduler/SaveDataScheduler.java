package ase.datasource.scheduler;

import ase.datasource.model.StoredRealtimeData;
import ase.datasource.repository.RealtimeDataRespository;
import ase.datasource.simulation.DataSimulation;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael on 21.06.2015.
 */
@Component
public class SaveDataScheduler {

    @Autowired
    private DataSimulation dataSimulation;

    @Autowired
    private RealtimeDataRespository realtimeDataRespository;

    //@Scheduled(fixedDelay = 250)
    @PostConstruct
    public void simulate() {

        if(realtimeDataRespository.count() > 0) {
            System.out.println("Delete collection \"simulation\" if you want to regenerate the data.");
            return;
        }


        System.out.println("Generating data");
        DateTime endSimulation = new DateTime(2015,6,30,23,59,0);

        List<String> lines = dataSimulation.getLines().stream().map(x->x.getName()).collect(Collectors.toList());

        while(endSimulation.isAfter(dataSimulation.getCurrentTime())) {

            for(String line : lines) {
                List<StoredRealtimeData> realtimeData = dataSimulation.move(line);
                realtimeDataRespository.save(realtimeData);
            }
        }
        System.out.println("Data generated");
    }
}
