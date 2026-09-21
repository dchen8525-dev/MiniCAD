package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved GEOMETRIC_TOLERANCE_TARGET.
 * Specifies the target of a geometric tolerance application.
 */
public final class StepGeometricToleranceTarget extends AbstractStepEntity {
    private final StepEntity targetShape;
    private final double magnitude;

    public StepGeometricToleranceTarget(int id, String name, StepEntity targetShape, double magnitude) {
        super(id, name);
        this.targetShape = targetShape;
        this.magnitude = magnitude;
    }

    public StepEntity getTargetShape() {
        return targetShape;
    }

    public double getMagnitude() {
        return magnitude;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("targetShape", targetShape);
        state.put("magnitude", magnitude);
        return state;
    }
}
