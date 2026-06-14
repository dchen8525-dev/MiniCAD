package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved BOUNDED_CURVE_2D.
 * A 2D curve with bounded extent.
 *
 * @param id step id
 * @param name step label
 * @param curve the underlying 2D curve
 */
/**
 * Resolved BOUNDED_CURVE_2D.
 * A 2D curve with bounded extent.
 *
 * @param id step id
 * @param name step label
 * @param curve the underlying 2D curve
 */
public final class StepBoundedCurve2D implements StepEntity {
    private final int id;
    private final String name;
    private final StepCurve curve;

    public StepBoundedCurve2D(int id, String name, StepCurve curve) {
        this.id = id;
        this.name = name;
        this.curve = curve;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepCurve getCurve() {
        return curve;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBoundedCurve2D that = (StepBoundedCurve2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(curve, that.curve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, curve);
    }

    @Override
    public String toString() {
        return "StepBoundedCurve2D{" + "id=" + id + "name=" + name + "curve=" + curve + "}";
    }
}
