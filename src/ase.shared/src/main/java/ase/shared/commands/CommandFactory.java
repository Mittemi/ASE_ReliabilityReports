package ase.shared.commands;

import ase.shared.commands.datasource.*;
import ase.shared.commands.reportstorage.*;
import ase.shared.dto.ReportMetadataDTO;
import ase.shared.model.ReportMetadata;
import ase.shared.model.analysis.Report;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Michael on 20.06.2015.
 */
@Component
public class CommandFactory {

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Bean
    private RestTemplate restTemplate() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
        converter.setObjectMapper(mapper);
        return new RestTemplate(Arrays.asList(converter));
        //return new RestTemplate();
    }

    //TODO: remove . from url (fiddler)
    private final String REPORT_STORAGE_URL = "http://localhost.:9100";
    private final String DATASOURCE_URL = "http://localhost.:9999";
    private final String NOTIFICATION_URL = "http://localhost.:9200";

    private <T> T autowire(T command) {
        autowireCapableBeanFactory.autowireBean(command);
        return command;
    }

    ///////////////
    //  REPORTS  //
    ///////////////

    public GetReportByIdCommand getReportByIdCommand(String reportId) {
        return autowire(new GetReportByIdCommand(REPORT_STORAGE_URL, reportId));
    }

    public CreateReportCommand createReportCommand(Report report) {
        return autowire(new CreateReportCommand(REPORT_STORAGE_URL, report));
    }

    public GetReportMetadataByIdCommand getReportMetadataByIdCommand(String reportId) {
        return autowire(new GetReportMetadataByIdCommand(REPORT_STORAGE_URL, reportId));
    }

    public CreateReportMetadataCommand createReportMetadataCommand(ReportMetadata report) {
        return autowire(new CreateReportMetadataCommand(REPORT_STORAGE_URL, report));
    }

    public UpdateReportMetadataCommand updateReportMetadataCommand(ReportMetadataDTO report) {
        return autowire(new UpdateReportMetadataCommand(REPORT_STORAGE_URL, report));
    }

    //////////////////
    //  DATASOURCE  //
    //////////////////

    public GetLinesCommand getLinesCommand() {
        return autowire(new GetLinesCommand(DATASOURCE_URL));
    }

    public GetStationsCommand getStationsCommand(String line) {
        return autowire(new GetStationsCommand(DATASOURCE_URL, line));
    }

    public GetStationsBetweenCommand getStationsBetweenCommand(String line, String stationA, String stationB) {
        return autowire(new GetStationsBetweenCommand(DATASOURCE_URL, line, stationA, stationB));
    }

    public GetDirectionsCommand getDirectionsCommand(String line) {
        return autowire(new GetDirectionsCommand(DATASOURCE_URL, line));
    }

    public GetRealtimeDataByStationAndTWCommand getRealtimeDataByStationAndTWCommand(String lineName, String direction, int stationNumber, Date from, Date to) {
        return autowire(new GetRealtimeDataByStationAndTWCommand(DATASOURCE_URL, lineName, direction, stationNumber, from, to));
    }

    public GetRealtimeDataByTrainAndTWCommand getRealtimeDataByTrainAndTWCommand(String lineName, String direction, int trainNumber, Date from, Date to) {
        return autowire(new GetRealtimeDataByTrainAndTWCommand(DATASOURCE_URL, lineName, direction, trainNumber, from, to));
    }
}
