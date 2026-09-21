package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved GEOMETRIC_TOLERANCE.
 * Base type for geometric dimensioning and tolerancing entities.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param magnitude tolerance magnitude value
 * @param toleratedShape tolerated shape aspect
 */
public final class StepGeometricTolerance extends AbstractStepEntity {
    private final double magnitude;
    private final StepEntity toleratedShape;

    public StepGeometricTolerance(int id, String name, double magnitude, StepEntity toleratedShape) {
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

    // Record-style accessors
    public StepEntity toleratedShape() {
        return toleratedShape;
    }

    public double magnitude() {
        return magnitude;
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
