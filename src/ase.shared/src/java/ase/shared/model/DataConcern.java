package ase.shared.model;

import ase.shared.enums.DataConcernType;

/**
 * Created by Michael on 20.06.2015.
 */
public class DataConcern {

    private DataConcernType concernType;

    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public DataConcernType getConcernType() {
        return concernType;
    }

    public void setConcernType(DataConcernType concernType) {
        this.concernType = concernType;
    }
}
