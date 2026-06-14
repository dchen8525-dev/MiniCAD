package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved GEOMETRIC_TOLERANCE.
 * Base type for geometric dimensioning and tolerancing entities.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param magnitude tolerance magnitude value
 * @param toleratedShape tolerated shape aspect
 */
/**
 * Resolved GEOMETRIC_TOLERANCE.
 * Base type for geometric dimensioning and tolerancing entities.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param magnitude tolerance magnitude value
 * @param toleratedShape tolerated shape aspect
 */
public final class StepGeometricTolerance implements StepEntity {
    private final int id;
    private final String name;
    private final double magnitude;
    private final StepEntity toleratedShape;

    public StepGeometricTolerance(int id, String name, double magnitude, StepEntity toleratedShape) {
        this.id = id;
        this.name = name;
        this.magnitude = magnitude;
        this.toleratedShape = toleratedShape;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public StepEntity getToleratedShape() {
        return toleratedShape;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeometricTolerance that = (StepGeometricTolerance) o;
        return id == that.id && Objects.equals(name, that.name) && magnitude == that.magnitude && Objects.equals(toleratedShape, that.toleratedShape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, magnitude, toleratedShape);
    }

    @Override
    public String toString() {
        return "StepGeometricTolerance{" + "id=" + id + "name=" + name + "magnitude=" + magnitude + "toleratedShape=" + toleratedShape + "}";
    }
}
