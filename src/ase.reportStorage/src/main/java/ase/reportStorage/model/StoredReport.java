package ase.reportStorage.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Michael on 21.05.2015.
 */
@Document(collection = "reports")
public class StoredReport extends ase.shared.model.analysis.Report {

    @Id
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
