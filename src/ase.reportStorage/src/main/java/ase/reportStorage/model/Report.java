package ase.reportStorage.model;

import ase.shared.dto.ReportDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Michael on 21.05.2015.
 */
@Document(collection = "reports")
public class Report extends ReportDTO {

    @Id
    private String id;
}
