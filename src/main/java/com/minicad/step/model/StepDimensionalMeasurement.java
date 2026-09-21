package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DIMENSIONAL_MEASUREMENT.
 * A dimensional measurement entity.
 *
 * @param id STEP instance id
 * @param name measurement name
 * @param measurementGeometry geometry being measured
 * @param measurementType measurement type (linear, angular, radius)
 * @param nominalValue nominal dimension value
 * @param upperTolerance upper tolerance limit
 * @param lowerTolerance lower tolerance limit
 * @param measuredValue measured value
 * @param measurementUnit measurement unit reference
 */
public final class StepDimensionalMeasurement extends AbstractStepEntity {
    private final StepEntity measurementGeometry;
    private final String measurementType;
    private final double nominalValue;
    private final double upperTolerance;
    private final double lowerTolerance;
    private final double measuredValue;
    private final StepEntity measurementUnit;

    public StepDimensionalMeasurement(int id, String name, StepEntity measurementGeometry, String measurementType, double nominalValue, double upperTolerance, double lowerTolerance, double measuredValue, StepEntity measurementUnit) {
        super(id, name);
        this.measurementGeometry = measurementGeometry;
        this.measurementType = measurementType;
        this.nominalValue = nominalValue;
        this.upperTolerance = upperTolerance;
        this.lowerTolerance = lowerTolerance;
        this.measuredValue = measuredValue;
        this.measurementUnit = measurementUnit;
    }

    public StepEntity getMeasurementGeometry() {
        return measurementGeometry;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public double getNominalValue() {
        return nominalValue;
    }

    public double getUpperTolerance() {
        return upperTolerance;
    }

    public double getLowerTolerance() {
        return lowerTolerance;
    }

    public double getMeasuredValue() {
        return measuredValue;
    }

    public StepEntity getMeasurementUnit() {
        return measurementUnit;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("measurementGeometry", measurementGeometry);
        state.put("measurementType", measurementType);
        state.put("nominalValue", nominalValue);
        state.put("upperTolerance", upperTolerance);
        state.put("lowerTolerance", lowerTolerance);
        state.put("measuredValue", measuredValue);
        state.put("measurementUnit", measurementUnit);
        return state;
    }
}
