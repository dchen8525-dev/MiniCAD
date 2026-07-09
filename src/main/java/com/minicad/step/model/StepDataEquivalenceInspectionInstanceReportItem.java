package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

/**
 * DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM entity model.
 * Report item for data equivalence inspection instance.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param measuredValueForInspectedElement reference to equivalence measured value select
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepDataEquivalenceInspectionInstanceReportItem implements StepEntity {
    private final int id;
    private final String name;
    private final Object measuredValueForInspectedElement; // equivalence_measured_value_select reference
    private final String entityName;

    public StepDataEquivalenceInspectionInstanceReportItem(
        int id,
        String name,
        Object measuredValueForInspectedElement,
        String entityName) {
        this.id = id;
        this.name = name;
        this.measuredValueForInspectedElement = measuredValueForInspectedElement;
        this.entityName = entityName;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Object getMeasuredValueForInspectedElement() {
        return measuredValueForInspectedElement;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDataEquivalenceInspectionInstanceReportItem that = (StepDataEquivalenceInspectionInstanceReportItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepDataEquivalenceInspectionInstanceReportItem{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", measuredValueForInspectedElement=" + measuredValueForInspectedElement +
            ", entityName='" + entityName + '\'' +
            '}';
    }
}