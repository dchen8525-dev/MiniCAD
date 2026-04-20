package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal SURFACE_STYLE_TRANSPARENT.
 *
 * @param id STEP instance id
 * @param transparency transparency factor
 */
public record StepSurfaceStyleTransparent(int id, double transparency) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
