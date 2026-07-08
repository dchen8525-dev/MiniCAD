package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved COMPOSITE_GROUP_TOLERANCE.
 * A composite tolerance that applies multiple tolerance requirements to a feature group.
 */
/**
 * Resolved COMPOSITE_GROUP_TOLERANCE.
 * A composite tolerance that applies multiple tolerance requirements to a feature group.
 */
public final class StepCompositeGroupTolerance implements StepEntity {
    private final int id;
    private final String name;
    private final double magnitude;
    private final StepEntity toleratedShape;

    public StepCompositeGroupTolerance(int id, String name, double magnitude, StepEntity toleratedShape) {
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
        StepCompositeGroupTolerance that = (StepCompositeGroupTolerance) o;
        return id == that.id && Objects.equals(name, that.name) && magnitude == that.magnitude && Objects.equals(toleratedShape, that.toleratedShape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, magnitude, toleratedShape);
    }

    @Override
    public String toString() {
        return "StepCompositeGroupTolerance{" + "id=" + id + "name=" + name + "magnitude=" + magnitude + "toleratedShape=" + toleratedShape + "}";
    }
}
