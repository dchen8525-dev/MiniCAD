package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR.
 *
 * @param id STEP instance id
 * @param ambientReflectance ambient reflectance factor
 * @param diffuseReflectance diffuse reflectance factor
 * @param specularReflectance specular reflectance factor
 * @param specularExponent specular exponent
 * @param specularColour specular colour
 */
public final class StepSurfaceStyleReflectanceAmbientDiffuseSpecular extends AbstractStepEntity {
    private final double ambientReflectance;
    private final double diffuseReflectance;
    private final double specularReflectance;
    private final double specularExponent;
    private final StepEntity specularColour;

    public StepSurfaceStyleReflectanceAmbientDiffuseSpecular(int id, double ambientReflectance, double diffuseReflectance, double specularReflectance, double specularExponent, StepEntity specularColour) {
        super(id, "");
        this.ambientReflectance = ambientReflectance;
        this.diffuseReflectance = diffuseReflectance;
        this.specularReflectance = specularReflectance;
        this.specularExponent = specularExponent;
        this.specularColour = specularColour;
    }

    public double getAmbientReflectance() {
        return ambientReflectance;
    }

    public double getDiffuseReflectance() {
        return diffuseReflectance;
    }

    public double getSpecularReflectance() {
        return specularReflectance;
    }

    public double getSpecularExponent() {
        return specularExponent;
    }

    public StepEntity getSpecularColour() {
        return specularColour;
    }

    // Record-style accessors
    public double ambientReflectance() {
        return ambientReflectance;
    }

    public double diffuseReflectance() {
        return diffuseReflectance;
    }

    public double specularReflectance() {
        return specularReflectance;
    }

    public double specularExponent() {
        return specularExponent;
    }

    public StepEntity specularColour() {
        return specularColour;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("ambientReflectance", ambientReflectance);
        state.put("diffuseReflectance", diffuseReflectance);
        state.put("specularReflectance", specularReflectance);
        state.put("specularExponent", specularExponent);
        state.put("specularColour", specularColour);
        return state;
    }
}
