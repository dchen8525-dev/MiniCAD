package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved GEOMETRIC_TOLERANCE_WITH_MAXIMUM_TOLERANCE.
 * A geometric tolerance with a specified maximum tolerance limit.
 */
/**
 * Resolved GEOMETRIC_TOLERANCE_WITH_MAXIMUM_TOLERANCE.
 * A geometric tolerance with a specified maximum tolerance limit.
 */
public final class StepGeometricToleranceWithMaximumTolerance implements StepEntity {
    private final int id;
    private final String name;
    private final String toleranceType;
    private final Double magnitude;
    private final StepEntity magnitudeUnit;
    private final StepEntity tolerancedFeature;
    private final Double maximumTolerance;

    public StepGeometricToleranceWithMaximumTolerance(int id, String name, String toleranceType, Double magnitude, StepEntity magnitudeUnit, StepEntity tolerancedFeature, Double maximumTolerance) {
        this.id = id;
        this.name = name;
        this.toleranceType = toleranceType;
        this.magnitude = magnitude;
        this.magnitudeUnit = magnitudeUnit;
        this.tolerancedFeature = tolerancedFeature;
        this.maximumTolerance = maximumTolerance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeometricToleranceWithMaximumTolerance that = (StepGeometricToleranceWithMaximumTolerance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(toleranceType, that.toleranceType) && Objects.equals(magnitude, that.magnitude) && Objects.equals(magnitudeUnit, that.magnitudeUnit) && Objects.equals(tolerancedFeature, that.tolerancedFeature) && Objects.equals(maximumTolerance, that.maximumTolerance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, toleranceType, magnitude, magnitudeUnit, tolerancedFeature, maximumTolerance);
    }

    @Override
    public String toString() {
        return "StepGeometricToleranceWithMaximumTolerance{" + "id=" + id + "name=" + name + "toleranceType=" + toleranceType + "magnitude=" + magnitude + "magnitudeUnit=" + magnitudeUnit + "tolerancedFeature=" + tolerancedFeature + "maximumTolerance=" + maximumTolerance + "}";
    }
}
