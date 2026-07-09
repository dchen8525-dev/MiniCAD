package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMeasurementPoint implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity pointPosition;
    private final String measurementType;
    private final StepEntity measurementDirection;
    private final StepEntity toleranceReference;
    private final double nominalValue;
    private final int measurementSequence;

    public StepMeasurementPoint(int id, String name, StepEntity pointPosition, String measurementType, StepEntity measurementDirection, StepEntity toleranceReference, double nominalValue, int measurementSequence) {
        this.id = id;
        this.name = name;
        this.pointPosition = pointPosition;
        this.measurementType = measurementType;
        this.measurementDirection = measurementDirection;
        this.toleranceReference = toleranceReference;
        this.nominalValue = nominalValue;
        this.measurementSequence = measurementSequence;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMeasurementPoint that = (StepMeasurementPoint) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(pointPosition, that.pointPosition) && Objects.equals(measurementType, that.measurementType) && Objects.equals(measurementDirection, that.measurementDirection) && Objects.equals(toleranceReference, that.toleranceReference) && nominalValue == that.nominalValue && measurementSequence == that.measurementSequence;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pointPosition, measurementType, measurementDirection, toleranceReference, nominalValue, measurementSequence);
    }

    @Override
    public String toString() {
        return "StepMeasurementPoint{" + "id=" + id + "name=" + name + "pointPosition=" + pointPosition + "measurementType=" + measurementType + "measurementDirection=" + measurementDirection + "toleranceReference=" + toleranceReference + "nominalValue=" + nominalValue + "measurementSequence=" + measurementSequence + "}";
    }
}