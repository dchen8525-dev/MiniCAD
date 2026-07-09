package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
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
public final class StepSurfaceStyleReflectanceAmbientDiffuseSpecular implements StepEntity {
    private final int id;
    private final double ambientReflectance;
    private final double diffuseReflectance;
    private final double specularReflectance;
    private final double specularExponent;
    private final StepEntity specularColour;

    public StepSurfaceStyleReflectanceAmbientDiffuseSpecular(int id, double ambientReflectance, double diffuseReflectance, double specularReflectance, double specularExponent, StepEntity specularColour) {
        this.id = id;
        this.ambientReflectance = ambientReflectance;
        this.diffuseReflectance = diffuseReflectance;
        this.specularReflectance = specularReflectance;
        this.specularExponent = specularExponent;
        this.specularColour = specularColour;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceStyleReflectanceAmbientDiffuseSpecular that = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) o;
        return id == that.id && ambientReflectance == that.ambientReflectance && diffuseReflectance == that.diffuseReflectance && specularReflectance == that.specularReflectance && specularExponent == that.specularExponent && Objects.equals(specularColour, that.specularColour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ambientReflectance, diffuseReflectance, specularReflectance, specularExponent, specularColour);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleReflectanceAmbientDiffuseSpecular{" + "id=" + id + "ambientReflectance=" + ambientReflectance + "diffuseReflectance=" + diffuseReflectance + "specularReflectance=" + specularReflectance + "specularExponent=" + specularExponent + "specularColour=" + specularColour + "}";
    }
}
