package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE.
 *
 * @param id STEP instance id
 * @param ambientReflectance ambient reflectance factor
 * @param diffuseReflectance diffuse reflectance factor
 */
public record StepSurfaceStyleReflectanceAmbientDiffuse(
        int id,
        double ambientReflectance,
        double diffuseReflectance
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
