package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal SURFACE_STYLE_REFLECTANCE_AMBIENT.
 *
 * @param id STEP instance id
 * @param ambientReflectance ambient reflectance factor
 */
/**
 * Minimal SURFACE_STYLE_REFLECTANCE_AMBIENT.
 *
 * @param id STEP instance id
 * @param ambientReflectance ambient reflectance factor
 */
public final class StepSurfaceStyleReflectanceAmbient implements StepEntity {
    private final int id;
    private final double ambientReflectance;

    public StepSurfaceStyleReflectanceAmbient(int id, double ambientReflectance) {
        this.id = id;
        this.ambientReflectance = ambientReflectance;
    }

    public int getId() {
        return id;
    }

    public double getAmbientReflectance() {
        return ambientReflectance;
    }

    public String getName() {
        return "";
    }

    // Record-style accessor
    public double ambientReflectance() {
        return ambientReflectance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceStyleReflectanceAmbient that = (StepSurfaceStyleReflectanceAmbient) o;
        return id == that.id && ambientReflectance == that.ambientReflectance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ambientReflectance);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleReflectanceAmbient{" + "id=" + id + "ambientReflectance=" + ambientReflectance + "}";
    }
}
