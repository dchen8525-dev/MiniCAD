package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved GEOMETRIC_MEASUREMENT.
 * A geometric measurement entity.
 *
 * @param id STEP instance id
 * @param name measurement name
 * @param measurementGeometry geometry being measured
 * @param geometricType geometric measurement type (flatness, roundness, position)
 * @param toleranceZone tolerance zone specification
 * @param measuredValue measured deviation value
 * @param measurementPoints measurement points used
 * @param passFailStatus pass/fail status result
 */
public final class StepGeometricMeasurement extends AbstractStepEntity {
    private final StepEntity measurementGeometry;
    private final String geometricType;
    private final StepEntity toleranceZone;
    private final double measuredValue;
    private final List<StepEntity> measurementPoints;
    private final String passFailStatus;

    public StepGeometricMeasurement(int id, String name, StepEntity measurementGeometry, String geometricType, StepEntity toleranceZone, double measuredValue, List<StepEntity> measurementPoints, String passFailStatus) {
        super(id, name);
        this.measurementGeometry = measurementGeometry;
        this.geometricType = geometricType;
        this.toleranceZone = toleranceZone;
        this.measuredValue = measuredValue;
        this.measurementPoints = measurementPoints == null ? null : java.util.List.copyOf(measurementPoints);
        this.passFailStatus = passFailStatus;
    }

    public StepEntity getMeasurementGeometry() {
        return measurementGeometry;
    }

    public String getGeometricType() {
        return geometricType;
    }

    public StepEntity getToleranceZone() {
        return toleranceZone;
    }

    public double getMeasuredValue() {
        return measuredValue;
    }

    public List<StepEntity> getMeasurementPoints() {
        return measurementPoints;
    }

    public String getPassFailStatus() {
        return passFailStatus;
    }

    // Record-style accessors
    public StepEntity measurementGeometry() {
        return measurementGeometry;
    }

    public String geometricType() {
        return geometricType;
    }

    public double measuredValue() {
        return measuredValue;
    }

    public List<StepEntity> measurementPoints() {
        return measurementPoints;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("measurementGeometry", measurementGeometry);
        state.put("geometricType", geometricType);
        state.put("toleranceZone", toleranceZone);
        state.put("measuredValue", measuredValue);
        state.put("measurementPoints", measurementPoints);
        state.put("passFailStatus", passFailStatus);
        return state;
    }
}
