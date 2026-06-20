package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT_ITEM entity model (ABSTRACT SUPERTYPE).
 * Base report item for data equivalence inspection criterion.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param measuredValue reference to measured value (for subtype A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE)
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepDataEquivalenceInspectionCriterionReportItem implements StepEntity {
    private final int id;
    private final String name;
    private final Object measuredValue; // equivalence_measured_value_select reference
    private final String entityName;

    public StepDataEquivalenceInspectionCriterionReportItem(
        int id,
        String name,
        Object measuredValue,
        String entityName) {
        this.id = id;
        this.name = name;
        this.measuredValue = measuredValue;
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

    public Object getMeasuredValue() {
        return measuredValue;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDataEquivalenceInspectionCriterionReportItem that = (StepDataEquivalenceInspectionCriterionReportItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepDataEquivalenceInspectionCriterionReportItem{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", measuredValue=" + measuredValue +
            ", entityName='" + entityName + '\'' +
            '}';
    }
}