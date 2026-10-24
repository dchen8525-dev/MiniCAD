package com.minicad.step.model;

import java.util.Map;

/**
 * DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT_ITEM entity model (ABSTRACT SUPERTYPE).
 * Base report item for data equivalence inspection criterion.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param measuredValue reference to measured value (for subtype A3M_EQUIVALENCE_CRITERION_REPORT_ITEM_WITH_MEASURED_VALUE)
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepDataEquivalenceInspectionCriterionReportItem extends AbstractStepEntity {
    private final Object measuredValue; // equivalence_measured_value_select reference
    private final String entityName;

    public StepDataEquivalenceInspectionCriterionReportItem(
        int id,
        String name,
        Object measuredValue,
        String entityName) {
        super(id, name);
        this.measuredValue = measuredValue;
        this.entityName = entityName;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "measuredValue", measuredValue, "entityName", entityName);
    }
}
