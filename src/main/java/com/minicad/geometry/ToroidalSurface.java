package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal toroidal surface representation.
 *
 * @param position torus placement
 * @param majorRadius distance from axis to tube center
 * @param minorRadius tube radius
 */
/**
 * Minimal toroidal surface representation.
 *
 * @param position torus placement
 * @param majorRadius distance from axis to tube center
 * @param minorRadius tube radius
 */
public final class ToroidalSurface implements SurfaceGeometry {
    private final Axis2Placement3D position;
    private final double majorRadius;
    private final double minorRadius;

    public ToroidalSurface(Axis2Placement3D position, double majorRadius, double minorRadius) {
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getMajorRadius() {
        return majorRadius;
    }

    public double getMinorRadius() {
        return minorRadius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToroidalSurface that = (ToroidalSurface) o;
        return Objects.equals(position, that.position) && majorRadius == that.majorRadius && minorRadius == that.minorRadius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, majorRadius, minorRadius);
    }

    @Override
    public String toString() {
        return "ToroidalSurface{" + "position=" + position + "majorRadius=" + majorRadius + "minorRadius=" + minorRadius + "}";
    }
}
