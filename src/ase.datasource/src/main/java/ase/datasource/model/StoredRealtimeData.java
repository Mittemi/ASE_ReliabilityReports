package ase.datasource.model;

import ase.shared.model.simulation.RealtimeData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Michael on 20.06.2015.
 */
@Document(collection = "realtime")
public class StoredRealtimeData extends RealtimeData {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
