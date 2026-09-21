package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved COMPOSITE_GROUP_TOLERANCE.
 * A composite tolerance that applies multiple tolerance requirements to a feature group.
 */
public final class StepCompositeGroupTolerance extends AbstractStepEntity {
    private final double magnitude;
    private final StepEntity toleratedShape;

    public StepCompositeGroupTolerance(int id, String name, double magnitude, StepEntity toleratedShape) {
        super(id, name);
        this.magnitude = magnitude;
        this.toleratedShape = toleratedShape;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public StepEntity getToleratedShape() {
        return toleratedShape;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("magnitude", magnitude);
        state.put("toleratedShape", toleratedShape);
        return state;
    }
}
