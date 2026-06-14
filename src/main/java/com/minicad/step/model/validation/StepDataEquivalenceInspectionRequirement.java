package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * DATA_EQUIVALENCE_INSPECTION_REQUIREMENT entity model.
 * Requirement for data equivalence inspection.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param appliedValues reference to SET of measure_representation_item (for A3M_EQUIVALENCE_INSPECTION_REQUIREMENT_WITH_VALUES)
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepDataEquivalenceInspectionRequirement implements StepEntity {
    private final int id;
    private final String name;
    private final List<Object> appliedValues; // SET [1:?] OF measure_representation_item references
    private final String entityName;

    public StepDataEquivalenceInspectionRequirement(
        int id,
        String name,
        List<Object> appliedValues,
        String entityName) {
        this.id = id;
        this.name = name;
        this.appliedValues = appliedValues;
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

    public List<Object> getAppliedValues() {
        return appliedValues;
    }

    public String getEntityName() {
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
            ", appliedValues=" + appliedValues +
            ", entityName='" + entityName + '\'' +
            '}';
    }
}