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
     * @param u normalized parameter along the swept curve
     * @param v revolution angle (radians)
     * @return point on the surface
     */
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint curvePoint = sweptCurve.pointAt(u);
        Vector3 axis = axisDirection.asVector();
        Vector3 offset = curvePoint.subtract(axisOrigin);
        Vector3 axial = axis.scale(offset.dot(axis));
        Vector3 radial = offset.subtract(axial);
        double cosU = Math.cos(v);
        double sinU = Math.sin(v);

        // Handle degenerate case: curvePoint on rotation axis
        if (radial.norm() < 1e-12) {
            return axisOrigin.add(axial);
        }

        // Rotate radial component around axis using perpendicular directions
        Vector3 perp1 = radial.normalize().asVector();
        Vector3 perp2 = axis.cross(perp1).normalize().asVector();
        Vector3 rotatedRadial = perp1.scale(radial.norm() * cosU).add(perp2.scale(radial.norm() * sinU));
        return axisOrigin.add(axial).add(rotatedRadial);
    }

    /**
     * {@inheritDoc}
     *
     * <p>U is swept through the generatrix's own sampling (its parameter domain
     * is curve-specific) and V over one full turn, {@code [0, 2PI)}.</p>
     */
    @Override
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        int uCount = Math.max(uSegments, 1);
        int vCount = Math.max(vSegments, 1);
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>(uCount + 1);
        for (int iu = 0; iu <= uCount; iu++) {
            double u = (double) iu / uCount;
            java.util.List<CartesianPoint> row = new java.util.ArrayList<>(vCount + 1);
            for (int iv = 0; iv <= vCount; iv++) {
                row.add(pointAt(u, 2.0 * Math.PI * iv / vCount));
            }
            grid.add(java.util.List.copyOf(row));
        }
        return java.util.List.copyOf(grid);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Revolving the generatrix gives {@code ∂P/∂u = α·e_r + t_axial·A} (in the
     * rotated frame) and {@code ∂P/∂v = |radial|·(A × e_r)}, so the normal is
     * {@code α·A − t_axial·e_r} — it only degenerates to a pure radial
     * direction when the generatrix runs parallel to the axis. The previous
     * implementation always returned the radial direction, which is wrong for
     * every other generatrix (a profile perpendicular to the axis revolves into
     * an annulus whose normal is the axis, not the radius).</p>
     */
    @Override
    public Vector3 normalAt(double u, double v) {
        Vector3 axis = axisDirection.asVector();

        // Radial direction and axial rate of the generatrix, in its own frame.
        Vector3 generatrixOffset = sweptCurve.pointAt(u).subtract(axisOrigin);
        Vector3 generatrixRadial = generatrixOffset.subtract(axis.scale(generatrixOffset.dot(axis)));
        double generatrixRadius = generatrixRadial.norm();
        if (generatrixRadius < Epsilon.EPS) {
            return axis; // generatrix on the axis: the surface is degenerate there
        }
        Vector3 tangent = sweptCurve.tangentAt(u);
        double axialRate = tangent.dot(axis);
        double radialRate = tangent.subtract(axis.scale(axialRate)).dot(generatrixRadial.normalize());

        // Same radial direction, rotated to the requested angle.
        Vector3 offset = pointAt(u, v).subtract(axisOrigin);
        Vector3 radial = offset.subtract(axis.scale(offset.dot(axis)));
        if (radial.norm() < Epsilon.EPS) {
            return axis;
        }
        Vector3 normal = axis.scale(radialRate).subtract(radial.normalize().scale(axialRate));
        if (normal.norm() <= Epsilon.EPS) {
            return axis;
        }
        return normal.normalize();
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
