package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.common.Epsilon;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Plane;
import com.minicad.geometry.SurfaceGeometry;

import java.util.List;
import java.util.Objects;

/**
 * Minimal face with optional planar validation.
 *
 * @param surface supporting surface
 * @param bounds face boundaries
 * @param sameSense whether the face orientation matches the surface normal
 */
/**
 * Minimal face with optional planar validation.
 *
 * @param surface supporting surface
 * @param bounds face boundaries
 * @param sameSense whether the face orientation matches the surface normal
 */
public final class Face {
    private final SurfaceGeometry surface;
    private final List<FaceBound> bounds;
    private final boolean sameSense;

    public Face(SurfaceGeometry surface, List<FaceBound> bounds, boolean sameSense) {
        this.surface = surface;
        this.bounds = bounds == null ? null : java.util.List.copyOf(bounds);
        this.sameSense = sameSense;
    }

    public SurfaceGeometry getSurface() {
        return surface;
    }

    public List<FaceBound> getBounds() {
        return bounds;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Face that = (Face) o;
        return Objects.equals(surface, that.surface) && Objects.equals(bounds, that.bounds) && sameSense == that.sameSense;
    }

    @Override
    public int hashCode() {
        return Objects.hash(surface, bounds, sameSense);
    }

    @Override
    public String toString() {
        return "Face{" + "surface=" + surface + "bounds=" + bounds + "sameSense=" + sameSense + "}";
    }
}
