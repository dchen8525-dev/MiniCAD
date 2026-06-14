package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE.
 *
 * @param id STEP instance id
 * @param ambientReflectance ambient reflectance factor
 * @param diffuseReflectance diffuse reflectance factor
 */
/**
 * Minimal SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE.
 *
 * @param id STEP instance id
 * @param ambientReflectance ambient reflectance factor
 * @param diffuseReflectance diffuse reflectance factor
 */
public final class StepSurfaceStyleReflectanceAmbientDiffuse implements StepEntity {
    private final int id;
    private final double ambientReflectance;
    private final double diffuseReflectance;

    public StepSurfaceStyleReflectanceAmbientDiffuse(int id, double ambientReflectance, double diffuseReflectance) {
        this.id = id;
        this.ambientReflectance = ambientReflectance;
        this.diffuseReflectance = diffuseReflectance;
    }

    public int getId() {
        return id;
    }

    public double getAmbientReflectance() {
        return ambientReflectance;
    }

    public double getDiffuseReflectance() {
        return diffuseReflectance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceStyleReflectanceAmbientDiffuse that = (StepSurfaceStyleReflectanceAmbientDiffuse) o;
        return id == that.id && ambientReflectance == that.ambientReflectance && diffuseReflectance == that.diffuseReflectance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ambientReflectance, diffuseReflectance);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleReflectanceAmbientDiffuse{" + "id=" + id + "ambientReflectance=" + ambientReflectance + "diffuseReflectance=" + diffuseReflectance + "}";
    }
}
