package ase.shared.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Michael on 20.06.2015.
 */
@Component
public class CommandFactory {

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    private final String REPORT_STORAGE_URL = "http://localhost:9000";

    public GetReportByIdCommand getReportByIdCommand(String reportId) {
        GetReportByIdCommand getReportByIdCommand = new GetReportByIdCommand(REPORT_STORAGE_URL, reportId);
        autowireCapableBeanFactory.autowireBean(getReportByIdCommand);
        return getReportByIdCommand;
    }
}
