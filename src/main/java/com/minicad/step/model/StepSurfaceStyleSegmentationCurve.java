package com.minicad.step.model;

/**
 * Minimal SURFACE_STYLE_SEGMENTATION_CURVE.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
public record StepSurfaceStyleSegmentationCurve(int id, StepCurveStyle style) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
