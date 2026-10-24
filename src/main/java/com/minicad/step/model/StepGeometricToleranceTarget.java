package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "targetShape", targetShape, "magnitude", magnitude);
    }
}
