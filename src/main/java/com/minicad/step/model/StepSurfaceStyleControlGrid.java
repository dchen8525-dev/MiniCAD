package com.minicad.step.model;

/**
 * Minimal SURFACE_STYLE_CONTROL_GRID.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
public record StepSurfaceStyleControlGrid(int id, StepCurveStyle style) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
