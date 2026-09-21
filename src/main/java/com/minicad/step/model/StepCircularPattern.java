package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CIRCULAR_PATTERN.
 * Represents a circular pattern feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param baseFeature base feature being patterned
 * @param axis pattern axis
 * @param angularSpacing angular spacing between features
 * @param count number of features
 */
public final class StepCircularPattern extends AbstractStepEntity {
    private final StepEntity baseFeature;
    private final StepEntity axis;
    private final Double angularSpacing;
    private final Integer count;

    public StepCircularPattern(int id, String name, StepEntity baseFeature, StepEntity axis, Double angularSpacing, Integer count) {
        super(id, name);
        this.baseFeature = baseFeature;
        this.axis = axis;
        this.angularSpacing = angularSpacing;
        this.count = count;
    }

    public StepEntity getBaseFeature() {
        return baseFeature;
    }

    public StepEntity getAxis() {
        return axis;
    }

    public Double getAngularSpacing() {
        return angularSpacing;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("baseFeature", baseFeature);
        state.put("axis", axis);
        state.put("angularSpacing", angularSpacing);
        state.put("count", count);
        return state;
    }
}
