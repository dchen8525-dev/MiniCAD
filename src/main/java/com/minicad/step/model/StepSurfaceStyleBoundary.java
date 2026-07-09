package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal SURFACE_STYLE_BOUNDARY.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
/**
 * Minimal SURFACE_STYLE_BOUNDARY.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
public final class StepSurfaceStyleBoundary implements StepEntity {
    private final int id;
    private final StepCurveStyle style;

    public StepSurfaceStyleBoundary(int id, StepCurveStyle style) {
        this.id = id;
        this.style = style;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepCurveStyle getStyle() {
        return style;
    }

    // Record-style accessor
    public StepCurveStyle style() {
        return style;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceStyleBoundary that = (StepSurfaceStyleBoundary) o;
        return id == that.id && Objects.equals(style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, style);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleBoundary{" + "id=" + id + "style=" + style + "}";
    }
}
