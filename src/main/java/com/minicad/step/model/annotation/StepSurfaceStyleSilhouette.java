package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal SURFACE_STYLE_SILHOUETTE.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
/**
 * Minimal SURFACE_STYLE_SILHOUETTE.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
public final class StepSurfaceStyleSilhouette implements StepEntity {
    private final int id;
    private final StepCurveStyle style;

    public StepSurfaceStyleSilhouette(int id, StepCurveStyle style) {
        this.id = id;
        this.style = style;
    }

    public int getId() {
        return id;
    }

    public StepCurveStyle getStyle() {
        return style;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceStyleSilhouette that = (StepSurfaceStyleSilhouette) o;
        return id == that.id && Objects.equals(style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, style);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleSilhouette{" + "id=" + id + "style=" + style + "}";
    }
}
