package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

/**
 * A3MA_EQUIVALENCE_INSPECTION_RESULT entity model.
 * SUBTYPE OF data_equivalence_inspection_result with criterion_inspected = a3m_equivalence_criterion_for_assembly.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param description optional description
 * @param result result value
 * @param criterionInspected reference to a3m_equivalence_criterion_for_assembly
 */
public final class StepA3maEquivalenceInspectionResult implements StepEntity {
    private final int id;
    private final String name;
    private final String description; // OPTIONAL from supertype
    private final Object result; // from supertype data_equivalence_inspection_result
    private final Object criterionInspected; // a3m_equivalence_criterion_for_assembly reference

    public StepA3maEquivalenceInspectionResult(
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
        StepA3maEquivalenceInspectionResult that = (StepA3maEquivalenceInspectionResult) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepA3maEquivalenceInspectionResult{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", result=" + result +
            ", criterionInspected=" + criterionInspected +
            '}';
    }
}