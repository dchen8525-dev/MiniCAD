package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal SURFACE_STYLE_SILHOUETTE.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
public final class StepSurfaceStyleSilhouette extends AbstractStepEntity {
    private final StepCurveStyle style;

    public StepSurfaceStyleSilhouette(int id, StepCurveStyle style) {
        super(id, "");
        this.style = style;
    }

    public StepCurveStyle getStyle() {
        return style;
    }

    // Record-style accessor
    public StepCurveStyle style() {
        return style;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "style", style);
    }
}
