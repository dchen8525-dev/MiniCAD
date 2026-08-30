package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Interface for supported face surface geometry. Parameters are always in the
 * implementation's <b>natural domain</b> — the values its {@code pointAt}
 * accepts (angle in radians for revolution surfaces, arc parameter for
 * extrusion profiles, knot-domain values for B-splines).
 */
public interface SurfaceGeometry {

    /**
     * Returns the surface point at natural-domain parameters {@code (u, v)}.
     * Each implementation defines its own domain and parameterization.
     *
     * @param u natural U-domain parameter
     * @param v natural V-domain parameter
     * @return point on the surface
     */
    CartesianPoint pointAt(double u, double v);

    /**
     * Returns the approximate bounding box of the surface by sampling.
     * Implementations may override with more efficient calculations.
     *
     * @return bounding box enclosing the surface
     */
    default BoundingBox3 boundingBox() {
        java.util.List<java.util.List<CartesianPoint>> grid = sampleGrid(32, 32);
        if (grid.isEmpty()) {
            return BoundingBox3.empty();
        }
        BoundingBox3 result = BoundingBox3.empty();
        for (java.util.List<CartesianPoint> row : grid) {
            for (CartesianPoint point : row) {
                result = result.expand(point);
            }
        }
        return result;
    }

    /**
     * Samples a grid of points on the surface.
     * Implementations should override with surface-specific sampling logic.
     *
     * @param uSegments number of segments along U direction
     * @param vSegments number of segments along V direction
     * @return grid of sampled points
     */
    default java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>();
        return java.util.List.copyOf(grid);
    }

    /**
     * Returns the unit surface normal at natural-domain parameters {@code (u, v)}.
     *
     * <p><b>Contract.</b> The normal is the normalized cross product of the two
     * parametric tangents, {@code normalize(∂P/∂u × ∂P/∂v)}, taken at the same
     * {@code (u, v)} and in the same natural domain as {@link #pointAt}. That
     * single rule is what ties the three methods together: a normal is only
     * meaningful for the point it belongs to, so an implementation may not
     * interpret {@code u} or {@code v} one way in {@code pointAt} and another
     * way here.</p>
     *
     * <p>Because the orientation follows each implementation's own parameter
     * order, it is not guaranteed to point "outward" — a surface swept as
     * {@code (azimuth, height)} and one swept as {@code (profile, angle)} come
     * out with opposite handedness, which is inherent to their
     * parameterizations rather than a defect. Face-level orientation is decided
     * downstream by the face's {@code sameSense} flag.</p>
     *
     * <p>Implementations override this with a closed-form normal. This default
     * estimates it by central differences on {@code pointAt}, so it stays valid
     * for any implementation, but it is only accurate to about the step size;
     * prefer a closed form wherever the parameterization is known.</p>
     *
     * @param u natural U-domain parameter
     * @param v natural V-domain parameter
     * @return unit normal vector
     */
    default Vector3 normalAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        // Central differences, with the step scaled to the parameter magnitude
        // so wide domains still get a usable delta.
        double h = 1.0e-5 * Math.max(1.0, Math.abs(u) + Math.abs(v));
        Vector3 tangentU = pointAt(u + h, v).subtract(pointAt(u - h, v));
        Vector3 tangentV = pointAt(u, v + h).subtract(pointAt(u, v - h));
        return normalFromTangents(tangentU, tangentV);
    }

    /**
     * Normalizes {@code ∂P/∂u × ∂P/∂v} into a unit normal, substituting a
     * well-defined axis-aligned normal where the parameterization degenerates
     * (a pole, a seam, or a collapsed tangent).
     *
     * <p>Closed-form {@code normalAt} implementations should route through this
     * so every surface degenerates the same way instead of each inventing its
     * own fallback.</p>
     *
     * @param tangentU partial derivative with respect to u
     * @param tangentV partial derivative with respect to v
     * @return unit normal vector
     */
    static Vector3 normalFromTangents(Vector3 tangentU, Vector3 tangentV) {
        if (tangentU.norm() <= Epsilon.EPS || tangentV.norm() <= Epsilon.EPS) {
            return new Vector3(0, 0, 1);
        }
        Vector3 normal = tangentU.cross(tangentV);
        double norm = normal.norm();
        if (norm <= Epsilon.EPS) {
            return new Vector3(0, 0, 1);
        }
        return normal.scale(1.0 / norm);
    }
}
