package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved RECTANGULAR_COMPOSITE_SURFACE.
 * A composite surface formed by combining rectangular surface patches.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param parentSurface the parent surface
 * @param u1 first u parameter boundary
 * @param u2 second u parameter boundary
 * @param v1 first v parameter boundary
 * @param v2 second v parameter boundary
 */
/**
 * Resolved RECTANGULAR_COMPOSITE_SURFACE.
 * A composite surface formed by combining rectangular surface patches.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param parentSurface the parent surface
 * @param u1 first u parameter boundary
 * @param u2 second u parameter boundary
 * @param v1 first v parameter boundary
 * @param v2 second v parameter boundary
 */
public final class StepRectangularCompositeSurface implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity parentSurface;
    private final double u1;
    private final double u2;
    private final double v1;
    private final double v2;

    public StepRectangularCompositeSurface(int id, String name, StepEntity parentSurface, double u1, double u2, double v1, double v2) {
        this.id = id;
        this.name = name;
        this.parentSurface = parentSurface;
        this.u1 = u1;
        this.u2 = u2;
        this.v1 = v1;
        this.v2 = v2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getParentSurface() {
        return parentSurface;
    }

    public double getU1() {
        return u1;
    }

    public double getU2() {
        return u2;
    }

    public double getV1() {
        return v1;
    }

    public double getV2() {
        return v2;
    }

    // Record-style accessor
    public StepEntity parentSurface() { return getParentSurface(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRectangularCompositeSurface that = (StepRectangularCompositeSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(parentSurface, that.parentSurface) && u1 == that.u1 && u2 == that.u2 && v1 == that.v1 && v2 == that.v2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentSurface, u1, u2, v1, v2);
    }

    @Override
    public String toString() {
        return "StepRectangularCompositeSurface{" + "id=" + id + "name=" + name + "parentSurface=" + parentSurface + "u1=" + u1 + "u2=" + u2 + "v1=" + v1 + "v2=" + v2 + "}";
    }
}
