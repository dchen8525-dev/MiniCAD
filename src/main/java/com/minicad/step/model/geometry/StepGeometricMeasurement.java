package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepGeometricMeasurement implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity measurementGeometry;
    private final String geometricType;
    private final StepEntity toleranceZone;
    private final double measuredValue;
    private final List<StepEntity> measurementPoints;
    private final String passFailStatus;

    public StepGeometricMeasurement(int id, String name, StepEntity measurementGeometry, String geometricType, StepEntity toleranceZone, double measuredValue, List<StepEntity> measurementPoints, String passFailStatus) {
        this.id = id;
        this.name = name;
        this.measurementGeometry = measurementGeometry;
        this.geometricType = geometricType;
        this.toleranceZone = toleranceZone;
        this.measuredValue = measuredValue;
        this.measurementPoints = measurementPoints == null ? null : java.util.List.copyOf(measurementPoints);
        this.passFailStatus = passFailStatus;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeometricMeasurement that = (StepGeometricMeasurement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(measurementGeometry, that.measurementGeometry) && Objects.equals(geometricType, that.geometricType) && Objects.equals(toleranceZone, that.toleranceZone) && measuredValue == that.measuredValue && Objects.equals(measurementPoints, that.measurementPoints) && Objects.equals(passFailStatus, that.passFailStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, measurementGeometry, geometricType, toleranceZone, measuredValue, measurementPoints, passFailStatus);
    }

    @Override
    public String toString() {
        return "StepGeometricMeasurement{" + "id=" + id + "name=" + name + "measurementGeometry=" + measurementGeometry + "geometricType=" + geometricType + "toleranceZone=" + toleranceZone + "measuredValue=" + measuredValue + "measurementPoints=" + measurementPoints + "passFailStatus=" + passFailStatus + "}";
    }
}