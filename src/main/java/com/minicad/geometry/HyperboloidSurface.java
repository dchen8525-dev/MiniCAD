package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal single-sheet hyperboloid surface (rotationally symmetric).
 * Parametrized as x^2/a^2 + y^2/a^2 - z^2/b^2 = 1 in local coordinates.
 *
 * @param position placement (axis is symmetry axis)
 * @param radius radius at z=0 (waist)
 * @param semiAxis b parameter controlling the z-spread rate
 */
/**
 * Minimal single-sheet hyperboloid surface (rotationally symmetric).
 * Parametrized as x^2/a^2 + y^2/a^2 - z^2/b^2 = 1 in local coordinates.
 *
 * @param position placement (axis is symmetry axis)
 * @param radius radius at z=0 (waist)
 * @param semiAxis b parameter controlling the z-spread rate
 */
public final class HyperboloidSurface implements SurfaceGeometry {
    private final Axis2Placement3D position;
    private final double radius;
    private final double semiAxis;

    public HyperboloidSurface(Axis2Placement3D position, double radius, double semiAxis) {
        this.position = position;
        this.radius = radius;
        this.semiAxis = semiAxis;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public double getSemiAxis() {
        return semiAxis;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return getPosition(); }
    public double radius() { return getRadius(); }
    public double semiAxis() { return getSemiAxis(); }

    /**
     * {@inheritDoc}
     *
     * <p>One-sheet hyperboloid about the placement axis: {@code u} is the
     * azimuth in radians, {@code v ∈ ℝ} the height parameter with
     * {@code r(v) = radius · √(1 + v²)} and {@code z = semiAxis · v} (the
     * waist circle of the given radius sits at {@code v = 0}).</p>
     */
    @Override
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        double ringRadius = radius * Math.sqrt(1.0 + v * v);
        double height = semiAxis * v;
        return position.getLocation()
                .add(position.xDirection().asVector().scale(ringRadius * Math.cos(u)))
                .add(position.yDirection().asVector().scale(ringRadius * Math.sin(u)))
                .add(position.getAxis().asVector().scale(height));
    }

    /**
     * {@inheritDoc}
     *
     * <p>Differentiating {@code r(v) = radius·√(1+v²)} and {@code z = semiAxis·v}
     * gives {@code ∂P/∂u = r·e_θ} and
     * {@code ∂P/∂v = radius·v/√(1+v²)·e_r + semiAxis·axis}; the normal is their
     * cross product.</p>
     */
    @Override
    public Vector3 normalAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        double root = Math.sqrt(1.0 + v * v);
        Vector3 eRadial = position.xDirection().asVector().scale(Math.cos(u))
                .add(position.yDirection().asVector().scale(Math.sin(u)));
        Vector3 eTheta = position.xDirection().asVector().scale(-Math.sin(u))
                .add(position.yDirection().asVector().scale(Math.cos(u)));
        Vector3 axis = position.getAxis().asVector();
        return SurfaceGeometry.normalFromTangents(
                eTheta.scale(radius * root),
                eRadial.scale(radius * v / root).add(axis.scale(semiAxis)));
    }

    /**
     * {@inheritDoc}
     *
     * <p>U sweeps one full turn {@code [0, 2PI]}; V walks the height parameter over
     * {@code [-1, 1]}, which brackets the waist circle at {@code v = 0}.</p>
     */
    @Override
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        return SurfaceGridSampling.sampleGrid(
                uSegments, vSegments, 0.0, 2.0 * Math.PI, -1.0, 1.0, this::pointAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HyperboloidSurface that = (HyperboloidSurface) o;
        return Objects.equals(position, that.position) && radius == that.radius && semiAxis == that.semiAxis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, radius, semiAxis);
    }

    @Override
    public String toString() {
        return "HyperboloidSurface{" + "position=" + position + "radius=" + radius + "semiAxis=" + semiAxis + "}";
    }
}
