package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SURFACE_OF_LINEAR_EXTRUSION.
 *
 * @param id step id
 * @param name step label
 * @param sweptCurve directrix curve
 * @param extrusionAxis extrusion vector
 */
/**
 * Resolved SURFACE_OF_LINEAR_EXTRUSION.
 *
 * @param id step id
 * @param name step label
 * @param sweptCurve directrix curve
 * @param extrusionAxis extrusion vector
 */
public final class StepSurfaceOfLinearExtrusion implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity sweptCurve;
    private final StepVector extrusionAxis;

    public StepSurfaceOfLinearExtrusion(int id, String name, StepEntity sweptCurve, StepVector extrusionAxis) {
        this.id = id;
        this.name = name;
        this.sweptCurve = sweptCurve;
        this.extrusionAxis = extrusionAxis;
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

    public StepVector getExtrusionAxis() {
        return extrusionAxis;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity sweptCurve() { return getSweptCurve(); }
    public StepVector extrusionAxis() { return getExtrusionAxis(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceOfLinearExtrusion that = (StepSurfaceOfLinearExtrusion) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sweptCurve, that.sweptCurve) && Objects.equals(extrusionAxis, that.extrusionAxis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sweptCurve, extrusionAxis);
    }

    @Override
    public String toString() {
        return "StepSurfaceOfLinearExtrusion{" + "id=" + id + "name=" + name + "sweptCurve=" + sweptCurve + "extrusionAxis=" + extrusionAxis + "}";
    }
}
