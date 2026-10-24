package com.minicad.step.model;

import java.util.Map;

/**
 * DATA_EQUIVALENCE_INSPECTION_REQUIREMENT entity model.
 * Requirement for data equivalence inspection.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param appliedValue reference to measure_representation_item (for A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES)
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepDataEquivalenceInspectionRequirement extends AbstractStepEntity {
    private final Object appliedValue; // measure_representation_item reference
    private final String entityName;

    public StepDataEquivalenceInspectionRequirement(
        int id,
        String name,
        Object appliedValue,
        String entityName) {
        super(id, name);
        this.appliedValue = appliedValue;
        this.entityName = entityName;
    }

    public Object getAppliedValue() {
        return appliedValue;
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
                "id", getId(), "name", getName(), "appliedValue", appliedValue, "entityName", entityName);
    }
}
