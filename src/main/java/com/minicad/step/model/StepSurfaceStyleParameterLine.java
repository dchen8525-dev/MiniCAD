package com.minicad.step.model;

/**
 * Minimal SURFACE_STYLE_PARAMETER_LINE.
 *
 * @param id STEP instance id
 * @param style referenced curve style
 */
public record StepSurfaceStyleParameterLine(int id, StepCurveStyle style) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
