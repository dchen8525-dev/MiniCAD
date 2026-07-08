package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * DATA_EQUIVALENCE_INSPECTION_REQUIREMENT entity model.
 * Requirement for data equivalence inspection.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param appliedValue reference to measure_representation_item (for A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES)
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepDataEquivalenceInspectionRequirement implements StepEntity {
    private final int id;
    private final String name;
    private final Object appliedValue; // measure_representation_item reference
    private final String entityName;

    public StepDataEquivalenceInspectionRequirement(
        int id,
        String name,
        Object appliedValue,
        String entityName) {
        this.id = id;
        this.name = name;
        this.appliedValue = appliedValue;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDataEquivalenceInspectionRequirement that = (StepDataEquivalenceInspectionRequirement) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepDataEquivalenceInspectionRequirement{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", appliedValue=" + appliedValue +
            ", entityName='" + entityName + '\'' +
            '}';
    }
}