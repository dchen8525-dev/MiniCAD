package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal paraboloid surface (rotationally symmetric).
 * Parametrized as z = (x^2 + y^2) / (4*f) in local coordinates.
 *
 * @param position placement (axis is symmetry axis)
 * @param focalLength focal distance, must be positive
 */
/**
 * Minimal paraboloid surface (rotationally symmetric).
 * Parametrized as z = (x^2 + y^2) / (4*f) in local coordinates.
 *
 * @param position placement (axis is symmetry axis)
 * @param focalLength focal distance, must be positive
 */
public final class ParaboloidSurface implements SurfaceGeometry {
    private final Axis2Placement3D position;
    private final double focalLength;

    public ParaboloidSurface(Axis2Placement3D position, double focalLength) {
        this.position = position;
        this.focalLength = focalLength;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getFocalLength() {
        return focalLength;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return getPosition(); }
    public double focalLength() { return getFocalLength(); }

    /**
     * {@inheritDoc}
     *
     * <p>Standard paraboloid {@code z = r² / (4f)} about the placement axis:
     * {@code u} is the azimuth in radians, {@code v ≥ 0} the radial growth
     * parameter with {@code r = 2f·v} and {@code z = f·v²}.</p>
     */
    @Override
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        double radius = 2.0 * focalLength * v;
        double height = focalLength * v * v;
        return position.getLocation()
                .add(position.xDirection().asVector().scale(radius * Math.cos(u)))
                .add(position.yDirection().asVector().scale(radius * Math.sin(u)))
                .add(position.getAxis().asVector().scale(height));
    }

    /**
     * {@inheritDoc}
     *
     * <p>Differentiating {@code r = 2fv} and {@code z = fv²} gives
     * {@code ∂P/∂u = r·e_θ} and {@code ∂P/∂v = 2f·e_r + 2fv·axis}; the normal
     * is their cross product.</p>
     */
    @Override
    public Vector3 normalAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        Vector3 eRadial = position.xDirection().asVector().scale(Math.cos(u))
                .add(position.yDirection().asVector().scale(Math.sin(u)));
        Vector3 eTheta = position.xDirection().asVector().scale(-Math.sin(u))
                .add(position.yDirection().asVector().scale(Math.cos(u)));
        Vector3 axis = position.getAxis().asVector();
        return SurfaceGeometry.normalFromTangents(
                eTheta.scale(2.0 * focalLength * v),
                eRadial.scale(2.0 * focalLength).add(axis.scale(2.0 * focalLength * v)));
    }

    @Override
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        int uCount = Math.max(uSegments, 1);
        int vCount = Math.max(vSegments, 1);
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>(uCount + 1);
        for (int iu = 0; iu <= uCount; iu++) {
            double u = Math.PI * 2.0 * iu / uCount;
            java.util.List<CartesianPoint> row = new java.util.ArrayList<>(vCount + 1);
            for (int iv = 0; iv <= vCount; iv++) {
                row.add(pointAt(u, (double) iv / vCount));
            }
            grid.add(java.util.List.copyOf(row));
        }
        return java.util.List.copyOf(grid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParaboloidSurface that = (ParaboloidSurface) o;
        return Objects.equals(position, that.position) && focalLength == that.focalLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, focalLength);
    }

    @Override
    public String toString() {
        return "ParaboloidSurface{" + "position=" + position + "focalLength=" + focalLength + "}";
    }
}
