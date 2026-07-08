package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal SURFACE_STYLE_SEGMENTATION_CURVE.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
/**
 * Minimal SURFACE_STYLE_SEGMENTATION_CURVE.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
public final class StepSurfaceStyleSegmentationCurve implements StepEntity {
    private final int id;
    private final StepCurveStyle style;

    public StepSurfaceStyleSegmentationCurve(int id, StepCurveStyle style) {
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
        StepSurfaceStyleSegmentationCurve that = (StepSurfaceStyleSegmentationCurve) o;
        return id == that.id && Objects.equals(style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, style);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleSegmentationCurve{" + "id=" + id + "style=" + style + "}";
    }
}
