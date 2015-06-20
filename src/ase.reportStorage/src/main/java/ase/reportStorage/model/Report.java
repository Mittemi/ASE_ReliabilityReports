package ase.reportStorage.model;

import ase.shared.dto.ReportDTO;
import ase.shared.model.Line;
import ase.shared.model.ReportTimeSpan;
import ase.shared.model.Station;
import ase.shared.model.Statistics;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 21.05.2015.
 */
@Document(collection = "reports")
public class Report extends ReportDTO {

    @Id
    private String id;
}
