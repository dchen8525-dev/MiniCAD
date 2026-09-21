package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE.
 * A geometric tolerance with datum reference entity.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param toleranceType tolerance type
 * @param magnitude tolerance magnitude
 * @param magnitudeUnit tolerance unit
 * @param tolerancedFeature the feature being toleranced
 * * @param datumReference datum reference entity
 */
public final class StepGeometricToleranceWithDatumReference extends AbstractStepEntity {
    private final String toleranceType;
    private final Double magnitude;
    private final StepEntity magnitudeUnit;
    private final StepEntity tolerancedFeature;
    private final StepEntity datumReference;

    public StepGeometricToleranceWithDatumReference(int id, String name, String toleranceType, Double magnitude, StepEntity magnitudeUnit, StepEntity tolerancedFeature, StepEntity datumReference) {
        super(id, name);
        this.toleranceType = toleranceType;
        this.magnitude = magnitude;
        this.magnitudeUnit = magnitudeUnit;
        this.tolerancedFeature = tolerancedFeature;
        this.datumReference = datumReference;
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

    public StepEntity getDatumReference() {
        return datumReference;
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
        state.put("datumReference", datumReference);
        return state;
    }
}
