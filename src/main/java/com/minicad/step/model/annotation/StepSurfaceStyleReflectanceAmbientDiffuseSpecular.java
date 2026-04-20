package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
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
public record StepSurfaceStyleReflectanceAmbientDiffuseSpecular(
        int id,
        double ambientReflectance,
        double diffuseReflectance,
        double specularReflectance,
        double specularExponent,
        StepEntity specularColour
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
