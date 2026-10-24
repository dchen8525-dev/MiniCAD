package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "magnitude", magnitude, "toleratedShape", toleratedShape);
    }
}
