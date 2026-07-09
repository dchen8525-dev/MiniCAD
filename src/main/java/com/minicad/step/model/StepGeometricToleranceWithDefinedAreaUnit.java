package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved GEOMETRIC_TOLERANCE_WITH_DEFINED_AREA_UNIT.
 * A geometric tolerance with a defined area unit for spatial application.
 */
/**
 * Resolved GEOMETRIC_TOLERANCE_WITH_DEFINED_AREA_UNIT.
 * A geometric tolerance with a defined area unit for spatial application.
 */
public final class StepGeometricToleranceWithDefinedAreaUnit implements StepEntity {
    private final int id;
    private final String name;
    private final String toleranceType;
    private final Double magnitude;
    private final StepEntity magnitudeUnit;
    private final StepEntity tolerancedFeature;
    private final StepEntity areaUnit;

    public StepGeometricToleranceWithDefinedAreaUnit(int id, String name, String toleranceType, Double magnitude, StepEntity magnitudeUnit, StepEntity tolerancedFeature, StepEntity areaUnit) {
        this.id = id;
        this.name = name;
        this.toleranceType = toleranceType;
        this.magnitude = magnitude;
        this.magnitudeUnit = magnitudeUnit;
        this.tolerancedFeature = tolerancedFeature;
        this.areaUnit = areaUnit;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeometricToleranceWithDefinedAreaUnit that = (StepGeometricToleranceWithDefinedAreaUnit) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(toleranceType, that.toleranceType) && Objects.equals(magnitude, that.magnitude) && Objects.equals(magnitudeUnit, that.magnitudeUnit) && Objects.equals(tolerancedFeature, that.tolerancedFeature) && Objects.equals(areaUnit, that.areaUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, toleranceType, magnitude, magnitudeUnit, tolerancedFeature, areaUnit);
    }

    @Override
    public String toString() {
        return "StepGeometricToleranceWithDefinedAreaUnit{" + "id=" + id + "name=" + name + "toleranceType=" + toleranceType + "magnitude=" + magnitude + "magnitudeUnit=" + magnitudeUnit + "tolerancedFeature=" + tolerancedFeature + "areaUnit=" + areaUnit + "}";
    }
}
