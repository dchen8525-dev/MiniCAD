package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SURFACE_OF_REVOLUTION.
 *
 * @param id step id
 * @param name step label
 * @param sweptCurve generatrix curve
 * @param axisPosition revolution axis
 */
/**
 * Resolved SURFACE_OF_REVOLUTION.
 *
 * @param id step id
 * @param name step label
 * @param sweptCurve generatrix curve
 * @param axisPosition revolution axis
 */
public final class StepSurfaceOfRevolution implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity sweptCurve;
    private final StepAxis1Placement axisPosition;

    public StepSurfaceOfRevolution(int id, String name, StepEntity sweptCurve, StepAxis1Placement axisPosition) {
        this.id = id;
        this.name = name;
        this.sweptCurve = sweptCurve;
        this.axisPosition = axisPosition;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSweptCurve() {
        return sweptCurve;
    }

    public StepAxis1Placement getAxisPosition() {
        return axisPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceOfRevolution that = (StepSurfaceOfRevolution) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sweptCurve, that.sweptCurve) && Objects.equals(axisPosition, that.axisPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sweptCurve, axisPosition);
    }

    @Override
    public String toString() {
        return "StepSurfaceOfRevolution{" + "id=" + id + "name=" + name + "sweptCurve=" + sweptCurve + "axisPosition=" + axisPosition + "}";
    }
}
