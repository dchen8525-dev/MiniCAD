package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved GEOMETRIC_TOLERANCE_WITH_DEFINED_AREA_UNIT.
 * A geometric tolerance with a defined area unit for spatial application.
 */
public final class StepGeometricToleranceWithDefinedAreaUnit extends AbstractStepEntity {
    private final String toleranceType;
    private final Double magnitude;
    private final StepEntity magnitudeUnit;
    private final StepEntity tolerancedFeature;
    private final StepEntity areaUnit;

    public StepGeometricToleranceWithDefinedAreaUnit(int id, String name, String toleranceType, Double magnitude, StepEntity magnitudeUnit, StepEntity tolerancedFeature, StepEntity areaUnit) {
        super(id, name);
        this.toleranceType = toleranceType;
        this.magnitude = magnitude;
        this.magnitudeUnit = magnitudeUnit;
        this.tolerancedFeature = tolerancedFeature;
        this.areaUnit = areaUnit;
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

    public StepEntity getAreaUnit() {
        return areaUnit;
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

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("toleranceType", toleranceType);
        state.put("magnitude", magnitude);
        state.put("magnitudeUnit", magnitudeUnit);
        state.put("tolerancedFeature", tolerancedFeature);
        state.put("areaUnit", areaUnit);
        return state;
    }
}
