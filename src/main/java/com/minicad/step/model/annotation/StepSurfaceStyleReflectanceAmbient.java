package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal SURFACE_STYLE_REFLECTANCE_AMBIENT.
 *
 * @param id STEP instance id
 * @param ambientReflectance ambient reflectance factor
 */
public record StepSurfaceStyleReflectanceAmbient(int id, double ambientReflectance) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
