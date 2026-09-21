package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepInspectionCriteria extends AbstractStepEntity {
    private final List<StepEntity> criteriaItems;
    private final StepEntity criteriaContext;
    private final List<Double> toleranceLimits;
    private final List<String> measurementMethod;

    public StepInspectionCriteria(int id, String name, List<StepEntity> criteriaItems, StepEntity criteriaContext, List<Double> toleranceLimits, List<String> measurementMethod) {
        super(id, name);
        this.criteriaItems = criteriaItems == null ? null : java.util.List.copyOf(criteriaItems);
        this.criteriaContext = criteriaContext;
        this.toleranceLimits = toleranceLimits == null ? null : java.util.List.copyOf(toleranceLimits);
        this.measurementMethod = measurementMethod == null ? null : java.util.List.copyOf(measurementMethod);
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("criteriaItems", criteriaItems);
        state.put("criteriaContext", criteriaContext);
        state.put("toleranceLimits", toleranceLimits);
        state.put("measurementMethod", measurementMethod);
        return state;
    }
}
