package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INSPECTION_CRITERIA.
 * An inspection criteria entity.
 *
 * @param id STEP instance id
 * @param name criteria name
 * @param criteriaItems list of criteria items
 * @param criteriaContext criteria context
 * @param toleranceLimits tolerance limits for each criterion
 * @param measurementMethod measurement method specifications
 */
/**
 * Resolved INSPECTION_CRITERIA.
 * An inspection criteria entity.
 *
 * @param id STEP instance id
 * @param name criteria name
 * @param criteriaItems list of criteria items
 * @param criteriaContext criteria context
 * @param toleranceLimits tolerance limits for each criterion
 * @param measurementMethod measurement method specifications
 */
public final class StepInspectionCriteria implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> criteriaItems;
    private final StepEntity criteriaContext;
    private final List<Double> toleranceLimits;
    private final List<String> measurementMethod;

    public StepInspectionCriteria(int id, String name, List<StepEntity> criteriaItems, StepEntity criteriaContext, List<Double> toleranceLimits, List<String> measurementMethod) {
        this.id = id;
        this.name = name;
        this.criteriaItems = criteriaItems == null ? null : java.util.List.copyOf(criteriaItems);
        this.criteriaContext = criteriaContext;
        this.toleranceLimits = toleranceLimits == null ? null : java.util.List.copyOf(toleranceLimits);
        this.measurementMethod = measurementMethod == null ? null : java.util.List.copyOf(measurementMethod);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getCriteriaItems() {
        return criteriaItems;
    }

    public StepEntity getCriteriaContext() {
        return criteriaContext;
    }

    public List<Double> getToleranceLimits() {
        return toleranceLimits;
    }

    public List<String> getMeasurementMethod() {
        return measurementMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInspectionCriteria that = (StepInspectionCriteria) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(criteriaItems, that.criteriaItems) && Objects.equals(criteriaContext, that.criteriaContext) && Objects.equals(toleranceLimits, that.toleranceLimits) && Objects.equals(measurementMethod, that.measurementMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, criteriaItems, criteriaContext, toleranceLimits, measurementMethod);
    }

    @Override
    public String toString() {
        return "StepInspectionCriteria{" + "id=" + id + "name=" + name + "criteriaItems=" + criteriaItems + "criteriaContext=" + criteriaContext + "toleranceLimits=" + toleranceLimits + "measurementMethod=" + measurementMethod + "}";
    }
}