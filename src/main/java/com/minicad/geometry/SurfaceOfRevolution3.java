package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal surface of revolution representation.
 *
 * @param sweptCurve generatrix curve
 * @param axisOrigin point on revolution axis
 * @param axisDirection revolution axis direction
 */
/**
 * Minimal surface of revolution representation.
 *
 * @param sweptCurve generatrix curve
 * @param axisOrigin point on revolution axis
 * @param axisDirection revolution axis direction
 */
public final class SurfaceOfRevolution3 implements SurfaceGeometry {
    private final Curve3 sweptCurve;
    private final CartesianPoint axisOrigin;
    private final Direction3 axisDirection;

    public SurfaceOfRevolution3(Curve3 sweptCurve, CartesianPoint axisOrigin, Direction3 axisDirection) {
        this.sweptCurve = sweptCurve;
        this.axisOrigin = axisOrigin;
        this.axisDirection = axisDirection;
    }

    public Curve3 getSweptCurve() {
        return sweptCurve;
    }

    public CartesianPoint getAxisOrigin() {
        return axisOrigin;
    }

    public Direction3 getAxisDirection() {
        return axisDirection;
    }

    // Record-style accessors
    public Curve3 sweptCurve() { return getSweptCurve(); }
    public CartesianPoint axisOrigin() { return getAxisOrigin(); }
    public Direction3 axisDirection() { return getAxisDirection(); }

    /**
     * Returns a point on the surface of revolution at the given parametric coordinates.
     *
     * @param u revolution angle (radians)
     * @param v parameter along the swept curve
     * @return point on the surface
     */
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint curvePoint = sweptCurve.pointAt(v);
        Vector3 axis = axisDirection.asVector();
        Vector3 offset = curvePoint.subtract(axisOrigin);
        Vector3 axial = axis.scale(offset.dot(axis));
        Vector3 radial = offset.subtract(axial);
        double cosU = Math.cos(u);
        double sinU = Math.sin(u);
        // Rotate radial component around axis using perpendicular directions
        Vector3 perp1 = radial.normalize().asVector();
        Vector3 perp2 = axis.cross(perp1).normalize().asVector();
        Vector3 rotatedRadial = perp1.scale(radial.norm() * cosU).add(perp2.scale(radial.norm() * sinU));
        return axisOrigin.add(axial).add(rotatedRadial);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurfaceOfRevolution3 that = (SurfaceOfRevolution3) o;
        return Objects.equals(sweptCurve, that.sweptCurve) && Objects.equals(axisOrigin, that.axisOrigin) && Objects.equals(axisDirection, that.axisDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sweptCurve, axisOrigin, axisDirection);
    }

    @Override
    public String toString() {
        return "SurfaceOfRevolution3{" + "sweptCurve=" + sweptCurve + "axisOrigin=" + axisOrigin + "axisDirection=" + axisDirection + "}";
    }
}
