package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import com.minicad.step.model.annotation.StepExternallyDefinedItem;
import java.util.Objects;

/**
 * EXTERNALLY_DEFINED_CONVERSION_BASED_UNIT entity model.
 * A conversion-based unit defined by an external source.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as LENGTH_UNIT
 * @param externallyDefinedItem reference to external definition
 */
public final class StepExternallyDefinedConversionBasedUnit implements StepEntity {
    private final int id;
    private final String name;
    private final String unitKind;
    private final StepExternallyDefinedItem externallyDefinedItem;

    public StepExternallyDefinedConversionBasedUnit(
        int id,
        String name,
        String unitKind,
        StepExternallyDefinedItem externallyDefinedItem) {
        this.id = id;
        this.name = name;
        this.unitKind = unitKind;
        this.externallyDefinedItem = externallyDefinedItem;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getUnitKind() {
        return unitKind;
    }

    public StepExternallyDefinedItem getExternallyDefinedItem() {
        return externallyDefinedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExternallyDefinedConversionBasedUnit that = (StepExternallyDefinedConversionBasedUnit) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepExternallyDefinedConversionBasedUnit{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", unitKind='" + unitKind + '\'' +
            ", externallyDefinedItem=" + externallyDefinedItem +
            '}';
    }
}