package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepGeometricToleranceWithDatumReference implements StepEntity {
    private final int id;
    private final String name;
    private final String toleranceType;
    private final Double magnitude;
    private final StepEntity magnitudeUnit;
    private final StepEntity tolerancedFeature;
    private final StepEntity datumReference;

    public StepGeometricToleranceWithDatumReference(int id, String name, String toleranceType, Double magnitude, StepEntity magnitudeUnit, StepEntity tolerancedFeature, StepEntity datumReference) {
        this.id = id;
        this.name = name;
        this.toleranceType = toleranceType;
        this.magnitude = magnitude;
        this.magnitudeUnit = magnitudeUnit;
        this.tolerancedFeature = tolerancedFeature;
        this.datumReference = datumReference;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeometricToleranceWithDatumReference that = (StepGeometricToleranceWithDatumReference) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(toleranceType, that.toleranceType) && Objects.equals(magnitude, that.magnitude) && Objects.equals(magnitudeUnit, that.magnitudeUnit) && Objects.equals(tolerancedFeature, that.tolerancedFeature) && Objects.equals(datumReference, that.datumReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, toleranceType, magnitude, magnitudeUnit, tolerancedFeature, datumReference);
    }

    @Override
    public String toString() {
        return "StepGeometricToleranceWithDatumReference{" + "id=" + id + "name=" + name + "toleranceType=" + toleranceType + "magnitude=" + magnitude + "magnitudeUnit=" + magnitudeUnit + "tolerancedFeature=" + tolerancedFeature + "datumReference=" + datumReference + "}";
    }
}