package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SURFACE_PATCH.
 * A bounded portion of a surface.
 *
 * @param id STEP instance id
 * @param name patch name
 * @param basisSurface the underlying surface
 * @param sameSense whether the patch has the same orientation as the basis surface
 */
/**
 * Resolved SURFACE_PATCH.
 * A bounded portion of a surface.
 *
 * @param id STEP instance id
 * @param name patch name
 * @param basisSurface the underlying surface
 * @param sameSense whether the patch has the same orientation as the basis surface
 */
public final class StepSurfacePatch implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity basisSurface;
    private final boolean sameSense;

    public StepSurfacePatch(int id, String name, StepEntity basisSurface, boolean sameSense) {
        this.id = id;
        this.name = name;
        this.basisSurface = basisSurface;
        this.sameSense = sameSense;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBasisSurface() {
        return basisSurface;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    // Record-style accessors
    public StepEntity basisSurface() { return getBasisSurface(); }
    public boolean sameSense() { return isSameSense(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfacePatch that = (StepSurfacePatch) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(basisSurface, that.basisSurface) && sameSense == that.sameSense;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, basisSurface, sameSense);
    }

    @Override
    public String toString() {
        return "StepSurfacePatch{" + "id=" + id + "name=" + name + "basisSurface=" + basisSurface + "sameSense=" + sameSense + "}";
    }
}
