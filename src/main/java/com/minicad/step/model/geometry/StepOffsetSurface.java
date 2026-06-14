package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved OFFSET_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param basisSurface basis surface
 * @param distance offset distance
 * @param selfIntersect self-intersection flag
 */
/**
 * Resolved OFFSET_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param basisSurface basis surface
 * @param distance offset distance
 * @param selfIntersect self-intersection flag
 */
public final class StepOffsetSurface implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity basisSurface;
    private final double distance;
    private final boolean selfIntersect;

    public StepOffsetSurface(int id, String name, StepEntity basisSurface, double distance, boolean selfIntersect) {
        this.id = id;
        this.name = name;
        this.basisSurface = basisSurface;
        this.distance = distance;
        this.selfIntersect = selfIntersect;
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

    public boolean isSelfIntersect() {
        return selfIntersect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOffsetSurface that = (StepOffsetSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(basisSurface, that.basisSurface) && distance == that.distance && selfIntersect == that.selfIntersect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, basisSurface, distance, selfIntersect);
    }

    @Override
    public String toString() {
        return "StepOffsetSurface{" + "id=" + id + "name=" + name + "basisSurface=" + basisSurface + "distance=" + distance + "selfIntersect=" + selfIntersect + "}";
    }
}
