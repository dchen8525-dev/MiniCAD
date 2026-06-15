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

    // Record-style accessors
    public Axis2Placement3D position() { return getPosition(); }
    public double majorRadius() { return getMajorRadius(); }
    public double minorRadius() { return getMinorRadius(); }

    /**
     * Returns a point on the toroidal surface at the given parametric coordinates.
     *
     * @param u revolution angle around the torus axis (radians)
     * @param v angle around the tube cross-section (radians)
     * @return point on the surface
     */
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint origin = position.location();
        Vector3 axis = position.axis().asVector();
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();
        double cosU = Math.cos(u);
        double sinU = Math.sin(u);
        double cosV = Math.cos(v);
        double sinV = Math.sin(v);
        double tubeCenterR = majorRadius + minorRadius * cosV;
        Vector3 radial = xDir.scale(tubeCenterR * cosU).add(yDir.scale(tubeCenterR * sinU));
        Vector3 tubeOffset = axis.scale(minorRadius * sinV);
        return origin.add(radial).add(tubeOffset);
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
