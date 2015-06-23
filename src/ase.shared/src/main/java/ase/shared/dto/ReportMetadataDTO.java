package ase.shared.dto;

import ase.shared.model.ReportMetadata;

import java.io.Serializable;

/**
 * Created by Michael on 23.06.2015.
 */
public class ReportMetadataDTO extends ReportMetadata<DataConcernDTO> implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
