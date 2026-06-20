package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * DATA_EQUIVALENCE_ASSESSMENT_SPECIFICATION entity model.
 * Base specification for data equivalence assessment.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param description optional description text
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepDataEquivalenceAssessmentSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final String description; // OPTIONAL
    private final String entityName;

    public StepDataEquivalenceAssessmentSpecification(
        int id,
        String name,
        String description,
        String entityName) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
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
        StepDataEquivalenceAssessmentSpecification that = (StepDataEquivalenceAssessmentSpecification) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepDataEquivalenceAssessmentSpecification{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", entityName='" + entityName + '\'' +
            '}';
    }
}