package gui;

import versatilesetup.SingleEventDataContainer;

public class ColoringFieldName {

    private final String fieldName;

    public ColoringFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValueForFieldName(SingleEventDataContainer object) {
        return object.getValue(fieldName);
    }
}
