package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
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
