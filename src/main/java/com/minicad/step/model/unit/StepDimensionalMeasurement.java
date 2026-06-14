package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepDimensionalMeasurement implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity measurementGeometry;
    private final String measurementType;
    private final double nominalValue;
    private final double upperTolerance;
    private final double lowerTolerance;
    private final double measuredValue;
    private final StepEntity measurementUnit;

    public StepDimensionalMeasurement(int id, String name, StepEntity measurementGeometry, String measurementType, double nominalValue, double upperTolerance, double lowerTolerance, double measuredValue, StepEntity measurementUnit) {
        this.id = id;
        this.name = name;
        this.measurementGeometry = measurementGeometry;
        this.measurementType = measurementType;
        this.nominalValue = nominalValue;
        this.upperTolerance = upperTolerance;
        this.lowerTolerance = lowerTolerance;
        this.measuredValue = measuredValue;
        this.measurementUnit = measurementUnit;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDimensionalMeasurement that = (StepDimensionalMeasurement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(measurementGeometry, that.measurementGeometry) && Objects.equals(measurementType, that.measurementType) && nominalValue == that.nominalValue && upperTolerance == that.upperTolerance && lowerTolerance == that.lowerTolerance && measuredValue == that.measuredValue && Objects.equals(measurementUnit, that.measurementUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, measurementGeometry, measurementType, nominalValue, upperTolerance, lowerTolerance, measuredValue, measurementUnit);
    }

    @Override
    public String toString() {
        return "StepDimensionalMeasurement{" + "id=" + id + "name=" + name + "measurementGeometry=" + measurementGeometry + "measurementType=" + measurementType + "nominalValue=" + nominalValue + "upperTolerance=" + upperTolerance + "lowerTolerance=" + lowerTolerance + "measuredValue=" + measuredValue + "measurementUnit=" + measurementUnit + "}";
    }
}