package ase.shared.commands;

import ase.shared.commands.analysis.RequestReportCommand;
import ase.shared.commands.datasource.*;
import ase.shared.commands.evaluation.GetDataConcernsCommand;
import ase.shared.commands.notification.CreateNotificationCommand;
import ase.shared.commands.notification.GetNotificationsByEmailCommand;
import ase.shared.commands.reportstorage.*;
import ase.shared.dto.AnalysisRequestDTO;
import ase.shared.dto.ReportMetadataDTO;
import ase.shared.model.ReportMetadata;
import ase.shared.model.analysis.Report;
import ase.shared.model.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

        return new RestTemplate();
    }

    private final String REPORT_STORAGE_URL = "http://localhost:9100";
    private final String DATASOURCE_URL = "http://localhost:9999";
    private final String NOTIFICATION_URL = "http://localhost:9200";
    private final String EVALUATION_URL = "http://localhost:9300";
    private final String ANALYSIS_URL = "http://localhost:9090";

    private <T> T autowire(T command) {
        autowireCapableBeanFactory.autowireBean(command);
        return command;
    }

    ///////////////
    //  REPORTS  //
    ///////////////

    public GetReportByIdCommand getReportByIdCommand(String id) {
        return autowire(new GetReportByIdCommand(REPORT_STORAGE_URL, id));
    }

    public CreateReportCommand createReportCommand(Report report) {
        return autowire(new CreateReportCommand(REPORT_STORAGE_URL, report));
    }

    public GetReportMetadataByIdCommand getReportMetadataByIdCommand(String id) {
        return autowire(new GetReportMetadataByIdCommand(REPORT_STORAGE_URL, id));
    }

    public GetReportMetadataByReportIdCommand getReportMetadataByReportIdCommand(String reportId) {
        return autowire(new GetReportMetadataByReportIdCommand(REPORT_STORAGE_URL, reportId));
    }

    public CreateReportMetadataCommand createReportMetadataCommand(ReportMetadata report) {
        return autowire(new CreateReportMetadataCommand(REPORT_STORAGE_URL, report));
    }

    public UpdateReportMetadataCommand updateReportMetadataCommand(ReportMetadataDTO report, String id) {
        return autowire(new UpdateReportMetadataCommand(REPORT_STORAGE_URL, id, report));
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

    public GetLineInfoCommand getLineInfoCommand(String lineName) {
        return autowire(new GetLineInfoCommand(DATASOURCE_URL, lineName));
    }

    ////////////////////
    //  NOTIFICATION  //
    ////////////////////

    public CreateNotificationCommand createNotificationCommand(Notification notification) {
        return autowire(new CreateNotificationCommand(NOTIFICATION_URL, notification));
    }

    public GetNotificationsByEmailCommand getNotificationsByEmailCommand(String email) {
        return autowire(new GetNotificationsByEmailCommand(NOTIFICATION_URL, email));
    }

    //////////////////
    //  EVALUATION  //
    //////////////////

    public GetDataConcernsCommand getDataConcernsCommand(String reportId) {
        return autowire(new GetDataConcernsCommand(EVALUATION_URL, reportId));
    }

    ////////////////
    //  ANALYSIS  //
    ////////////////

    public RequestReportCommand requestReportCommand(AnalysisRequestDTO analysisRequestDTO, String priority) {
        return autowire(new RequestReportCommand(ANALYSIS_URL, analysisRequestDTO, priority));
    }
}
