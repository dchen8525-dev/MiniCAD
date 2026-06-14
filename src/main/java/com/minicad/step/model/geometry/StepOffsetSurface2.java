package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved OFFSET_SURFACE_2.
 * An offset surface at a given distance from a basis surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param basisSurface the underlying surface
 * @param distance the offset distance
 * @param sameSense whether the offset surface has the same orientation as the basis surface
 */
/**
 * Resolved OFFSET_SURFACE_2.
 * An offset surface at a given distance from a basis surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param basisSurface the underlying surface
 * @param distance the offset distance
 * @param sameSense whether the offset surface has the same orientation as the basis surface
 */
public final class StepOffsetSurface2 implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity basisSurface;
    private final double distance;
    private final boolean sameSense;

    public StepOffsetSurface2(int id, String name, StepEntity basisSurface, double distance, boolean sameSense) {
        this.id = id;
        this.name = name;
        this.basisSurface = basisSurface;
        this.distance = distance;
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

    public double getDistance() {
        return distance;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOffsetSurface2 that = (StepOffsetSurface2) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(basisSurface, that.basisSurface) && distance == that.distance && sameSense == that.sameSense;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, basisSurface, distance, sameSense);
    }

    @Override
    public String toString() {
        return "StepOffsetSurface2{" + "id=" + id + "name=" + name + "basisSurface=" + basisSurface + "distance=" + distance + "sameSense=" + sameSense + "}";
    }
}
