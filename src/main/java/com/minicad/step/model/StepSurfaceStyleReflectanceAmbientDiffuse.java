package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE.
 *
 * @param id STEP instance id
 * @param ambientReflectance ambient reflectance factor
 * @param diffuseReflectance diffuse reflectance factor
 */
public final class StepSurfaceStyleReflectanceAmbientDiffuse extends AbstractStepEntity {
    private final double ambientReflectance;
    private final double diffuseReflectance;

    public StepSurfaceStyleReflectanceAmbientDiffuse(int id, double ambientReflectance, double diffuseReflectance) {
        super(id, "");
        this.ambientReflectance = ambientReflectance;
        this.diffuseReflectance = diffuseReflectance;
    }

    public double getAmbientReflectance() {
        return ambientReflectance;
    }

    public double getDiffuseReflectance() {
        return diffuseReflectance;
    }

    // Record-style accessors
    public double ambientReflectance() {
        return ambientReflectance;
    }

    public double diffuseReflectance() {
        return diffuseReflectance;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("ambientReflectance", ambientReflectance);
        state.put("diffuseReflectance", diffuseReflectance);
        return state;
    }
}
