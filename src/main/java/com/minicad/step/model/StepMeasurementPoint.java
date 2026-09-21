package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MEASUREMENT_POINT.
 * A measurement point entity.
 *
 * @param id STEP instance id
 * @param name point name
 * @param pointPosition measurement point position geometry
 * @param measurementType measurement type (dimensional, geometric, surface)
 * @param measurementDirection measurement direction vector
 * @param toleranceReference tolerance reference for this point
 * @param nominalValue nominal value for measurement
 * @param measurementSequence measurement sequence order
 */
public final class StepMeasurementPoint extends AbstractStepEntity {
    private final StepEntity pointPosition;
    private final String measurementType;
    private final StepEntity measurementDirection;
    private final StepEntity toleranceReference;
    private final double nominalValue;
    private final int measurementSequence;

    public StepMeasurementPoint(int id, String name, StepEntity pointPosition, String measurementType, StepEntity measurementDirection, StepEntity toleranceReference, double nominalValue, int measurementSequence) {
        super(id, name);
        this.pointPosition = pointPosition;
        this.measurementType = measurementType;
        this.measurementDirection = measurementDirection;
        this.toleranceReference = toleranceReference;
        this.nominalValue = nominalValue;
        this.measurementSequence = measurementSequence;
    }

    public StepEntity getPointPosition() {
        return pointPosition;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public StepEntity getMeasurementDirection() {
        return measurementDirection;
    }

    public StepEntity getToleranceReference() {
        return toleranceReference;
    }

    public double getNominalValue() {
        return nominalValue;
    }

    public int getMeasurementSequence() {
        return measurementSequence;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("pointPosition", pointPosition);
        state.put("measurementType", measurementType);
        state.put("measurementDirection", measurementDirection);
        state.put("toleranceReference", toleranceReference);
        state.put("nominalValue", nominalValue);
        state.put("measurementSequence", measurementSequence);
        return state;
    }
}
