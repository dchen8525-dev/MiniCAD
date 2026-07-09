package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal SURFACE_STYLE_TRANSPARENT.
 *
 * @param id STEP instance id
 * @param transparency transparency factor
 */
/**
 * Minimal SURFACE_STYLE_TRANSPARENT.
 *
 * @param id STEP instance id
 * @param transparency transparency factor
 */
public final class StepSurfaceStyleTransparent implements StepEntity {
    private final int id;
    private final double transparency;

    public StepSurfaceStyleTransparent(int id, double transparency) {
        this.id = id;
        this.transparency = transparency;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public double getTransparency() {
        return transparency;
    }

    // Record-style accessor
    public double transparency() {
        return transparency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceStyleTransparent that = (StepSurfaceStyleTransparent) o;
        return id == that.id && transparency == that.transparency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transparency);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleTransparent{" + "id=" + id + "transparency=" + transparency + "}";
    }
}
