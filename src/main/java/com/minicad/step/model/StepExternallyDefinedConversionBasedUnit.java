package com.minicad.step.model;

import java.util.Map;

/**
 * EXTERNALLY_DEFINED_CONVERSION_BASED_UNIT entity model.
 * A conversion-based unit defined by an external source.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as LENGTH_UNIT
 * @param externallyDefinedItem reference to external definition
 */
public final class StepExternallyDefinedConversionBasedUnit extends AbstractStepEntity {
    private final String unitKind;
    private final StepExternallyDefinedItem externallyDefinedItem;

    public StepExternallyDefinedConversionBasedUnit(
        int id,
        String name,
        String unitKind,
        StepExternallyDefinedItem externallyDefinedItem) {
        super(id, name);
        this.unitKind = unitKind;
        this.externallyDefinedItem = externallyDefinedItem;
    }

    public String getUnitKind() {
        return unitKind;
    }

    public StepExternallyDefinedItem getExternallyDefinedItem() {
        return externallyDefinedItem;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "unitKind", unitKind,
                "externallyDefinedItem", externallyDefinedItem);
    }
}
