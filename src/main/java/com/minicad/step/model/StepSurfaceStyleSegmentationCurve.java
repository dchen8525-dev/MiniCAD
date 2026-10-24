package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal SURFACE_STYLE_SEGMENTATION_CURVE.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
public final class StepSurfaceStyleSegmentationCurve extends AbstractStepEntity {
    private final StepCurveStyle style;

    public StepSurfaceStyleSegmentationCurve(int id, StepCurveStyle style) {
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
