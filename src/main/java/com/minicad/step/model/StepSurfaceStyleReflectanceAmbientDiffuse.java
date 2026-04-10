package com.minicad.step.model;

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
