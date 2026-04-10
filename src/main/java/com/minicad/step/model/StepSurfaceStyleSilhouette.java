package com.minicad.step.model;

/**
 * Minimal SURFACE_STYLE_SILHOUETTE.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
public record StepSurfaceStyleSilhouette(int id, StepCurveStyle style) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
