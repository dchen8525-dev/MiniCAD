package com.minicad.geometry;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal infinite conical surface representation.
 *
 * @param position cone placement
 * @param radius radius at placement origin
 * @param semiAngle cone semi-angle in radians
 */
/**
 * Minimal infinite conical surface representation.
 *
 * @param position cone placement
 * @param radius radius at placement origin
 * @param semiAngle cone semi-angle in radians
 */
public final class ConicalSurface implements SurfaceGeometry {
    private final Axis2Placement3D position;
    private final double radius;
    private final double semiAngle;

    public ConicalSurface(Axis2Placement3D position, double radius, double semiAngle) {
        this.position = position;
        this.radius = radius;
        this.semiAngle = semiAngle;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public double getSemiAngle() {
        return semiAngle;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return position; }
    public double radius() { return radius; }
    public double semiAngle() { return semiAngle; }

    /**
     * Returns a point on the conical surface at the given parametric coordinates.
     *
     * @param u angle around the cone axis (radians)
     * @param v height along the cone axis
     * @return point on the surface
     */
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint origin = position.getLocation();
        Vector3 axis = position.getAxis().asVector();
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();
        double r = radius + v * Math.tan(semiAngle);
        double cosU = Math.cos(u);
        double sinU = Math.sin(u);
        Vector3 radial = xDir.scale(r * cosU).add(yDir.scale(r * sinU));
        return origin.add(radial).add(axis.scale(v));
    }

    /**
     * {@inheritDoc}
     *
     * <p>A cone is developable, so its normal is constant along a generator and
     * depends only on the azimuth — hence the delegation to
     * {@link #normalAt(double)}. Without this override the interface's
     * finite-difference default would win and the closed form below would never
     * run.</p>
     */
    @Override
    public Vector3 normalAt(double u, double v) {
        return normalAt(u);
    }

    /**
     * Returns the surface normal at the given parametric coordinates.
     *
     * @param u angle around the cone axis (radians)
     * @return unit normal vector
     */
    public Vector3 normalAt(double u) {
        Preconditions.requireFinite(u, "u");
        Vector3 axis = position.getAxis().asVector();
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();
        double cosU = Math.cos(u);
        double sinU = Math.sin(u);
        Vector3 radial = xDir.scale(cosU).add(yDir.scale(sinU));
        Vector3 axial = axis.scale(-Math.tan(semiAngle));
        return radial.add(axial).normalize().asVector();
    }

    /**
     * {@inheritDoc}
     *
     * <p>U sweeps one full turn {@code [0, 2PI]}; V walks the finite axial window
     * {@code [-10, 10]}, the extent over which a cone of unlimited height is drawn.</p>
     */
    @Override
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        return SurfaceGridSampling.sampleGrid(
                uSegments, vSegments, 0.0, 2.0 * Math.PI, -10.0, 10.0, this::pointAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConicalSurface that = (ConicalSurface) o;
        return Objects.equals(position, that.position) && radius == that.radius && semiAngle == that.semiAngle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, radius, semiAngle);
    }

    @Override
    public String toString() {
        return "ConicalSurface{" + "position=" + position + "radius=" + radius + "semiAngle=" + semiAngle + "}";
    }
}
