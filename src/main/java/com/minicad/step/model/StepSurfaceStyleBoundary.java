package com.minicad.step.model;

/**
 * Minimal SURFACE_STYLE_BOUNDARY.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
public record StepSurfaceStyleBoundary(int id, StepCurveStyle style) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
