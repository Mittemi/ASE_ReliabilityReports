package ase.reportStorage.model;

import ase.shared.model.ReportMetadata;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Michael on 23.06.2015.
 */
@Document(collection = "reportmetedata")
public class StoredReportMetadata extends ReportMetadata {
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
