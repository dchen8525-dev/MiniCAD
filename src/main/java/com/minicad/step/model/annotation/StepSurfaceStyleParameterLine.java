package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
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
