package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal SURFACE_STYLE_REFLECTANCE_AMBIENT.
 *
 * @param id STEP instance id
 * @param ambientReflectance ambient reflectance factor
 */
public final class StepSurfaceStyleReflectanceAmbient extends AbstractStepEntity {
    private final double ambientReflectance;

    public StepSurfaceStyleReflectanceAmbient(int id, double ambientReflectance) {
        super(id, "");
        this.ambientReflectance = ambientReflectance;
    }

    public double getAmbientReflectance() {
        return ambientReflectance;
    }

    // Record-style accessor
    public double ambientReflectance() {
        return ambientReflectance;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "ambientReflectance", ambientReflectance);
    }
}
