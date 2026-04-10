package com.minicad.step.model;

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
