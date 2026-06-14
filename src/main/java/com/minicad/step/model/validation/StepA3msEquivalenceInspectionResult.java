package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * A3MS_EQUIVALENCE_INSPECTION_RESULT entity model.
 * SUBTYPE OF data_equivalence_inspection_result with criterion_inspected = a3m_equivalence_criterion_for_shape.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param description optional description
 * @param result result value
 * @param criterionInspected reference to a3m_equivalence_criterion_for_shape
 */
public final class StepA3msEquivalenceInspectionResult implements StepEntity {
    private final int id;
    private final String name;
    private final String description; // OPTIONAL from supertype
    private final Object result; // from supertype data_equivalence_inspection_result
    private final Object criterionInspected; // a3m_equivalence_criterion_for_shape reference

    public StepA3msEquivalenceInspectionResult(
        int id,
        String name,
        String description,
        Object result,
        Object criterionInspected) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.result = result;
        this.criterionInspected = criterionInspected;
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

    public Object getResult() {
        return result;
    }

    public Object getCriterionInspected() {
        return criterionInspected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepA3msEquivalenceInspectionResult that = (StepA3msEquivalenceInspectionResult) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepA3msEquivalenceInspectionResult{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", result=" + result +
            ", criterionInspected=" + criterionInspected +
            '}';
    }
}