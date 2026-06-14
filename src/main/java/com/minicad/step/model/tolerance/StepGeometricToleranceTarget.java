package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved GEOMETRIC_TOLERANCE_TARGET.
 * Specifies the target of a geometric tolerance application.
 */
/**
 * Resolved GEOMETRIC_TOLERANCE_TARGET.
 * Specifies the target of a geometric tolerance application.
 */
public final class StepGeometricToleranceTarget implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity targetShape;
    private final double magnitude;

    public StepGeometricToleranceTarget(int id, String name, StepEntity targetShape, double magnitude) {
        this.id = id;
        this.name = name;
        this.targetShape = targetShape;
        this.magnitude = magnitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getTargetShape() {
        return targetShape;
    }

    public double getMagnitude() {
        return magnitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeometricToleranceTarget that = (StepGeometricToleranceTarget) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(targetShape, that.targetShape) && magnitude == that.magnitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, targetShape, magnitude);
    }

    @Override
    public String toString() {
        return "StepGeometricToleranceTarget{" + "id=" + id + "name=" + name + "targetShape=" + targetShape + "magnitude=" + magnitude + "}";
    }
}
