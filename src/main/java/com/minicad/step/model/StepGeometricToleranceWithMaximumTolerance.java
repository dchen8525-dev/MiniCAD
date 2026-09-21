package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved GEOMETRIC_TOLERANCE_WITH_MAXIMUM_TOLERANCE.
 * A geometric tolerance with a specified maximum tolerance limit.
 */
public final class StepGeometricToleranceWithMaximumTolerance extends AbstractStepEntity {
    private final String toleranceType;
    private final Double magnitude;
    private final StepEntity magnitudeUnit;
    private final StepEntity tolerancedFeature;
    private final Double maximumTolerance;

    public StepGeometricToleranceWithMaximumTolerance(int id, String name, String toleranceType, Double magnitude, StepEntity magnitudeUnit, StepEntity tolerancedFeature, Double maximumTolerance) {
        super(id, name);
        this.toleranceType = toleranceType;
        this.magnitude = magnitude;
        this.magnitudeUnit = magnitudeUnit;
        this.tolerancedFeature = tolerancedFeature;
        this.maximumTolerance = maximumTolerance;
    }

    public String getToleranceType() {
        return toleranceType;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public StepEntity getMagnitudeUnit() {
        return magnitudeUnit;
    }

    public StepEntity getTolerancedFeature() {
        return tolerancedFeature;
    }

    public Double getMaximumTolerance() {
        return maximumTolerance;
    }

    // Record-style accessors
    public StepEntity tolerancedFeature() {
        return tolerancedFeature;
    }

    public String toleranceType() {
        return toleranceType;
    }

    public Double magnitude() {
        return magnitude;
    }

    public Double maximumTolerance() {
        return maximumTolerance;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("toleranceType", toleranceType);
        state.put("magnitude", magnitude);
        state.put("magnitudeUnit", magnitudeUnit);
        state.put("tolerancedFeature", tolerancedFeature);
        state.put("maximumTolerance", maximumTolerance);
        return state;
    }
}
