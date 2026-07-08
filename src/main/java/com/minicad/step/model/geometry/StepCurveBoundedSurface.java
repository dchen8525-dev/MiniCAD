package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal CURVE_BOUNDED_SURFACE parse-only surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param basisSurface surface being bounded
 * @param boundaries boundary curves
 * @param implicitOuter whether an implicit outer boundary is present
 */
/**
 * Minimal CURVE_BOUNDED_SURFACE parse-only surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param basisSurface surface being bounded
 * @param boundaries boundary curves
 * @param implicitOuter whether an implicit outer boundary is present
 */
public final class StepCurveBoundedSurface implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity basisSurface;
    private final List<StepEntity> boundaries;
    private final boolean implicitOuter;

    public StepCurveBoundedSurface(int id, String name, StepEntity basisSurface, List<StepEntity> boundaries, boolean implicitOuter) {
        this.id = id;
        this.name = name;
        this.basisSurface = basisSurface;
        this.boundaries = boundaries == null ? null : java.util.List.copyOf(boundaries);
        this.implicitOuter = implicitOuter;
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

    public List<StepEntity> getBoundaries() {
        return boundaries;
    }

    public boolean isImplicitOuter() {
        return implicitOuter;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisSurface() { return getBasisSurface(); }
    public List<StepEntity> boundaries() { return getBoundaries(); }
    public boolean implicitOuter() { return isImplicitOuter(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCurveBoundedSurface that = (StepCurveBoundedSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(basisSurface, that.basisSurface) && Objects.equals(boundaries, that.boundaries) && implicitOuter == that.implicitOuter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, basisSurface, boundaries, implicitOuter);
    }

    @Override
    public String toString() {
        return "StepCurveBoundedSurface{" + "id=" + id + "name=" + name + "basisSurface=" + basisSurface + "boundaries=" + boundaries + "implicitOuter=" + implicitOuter + "}";
    }
}
