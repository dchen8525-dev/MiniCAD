package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal 3D hyperbola representation.
 * A hyperbola is a conic section defined by two semi-axes.
 *
 * @param position hyperbola placement (center at origin, transverse axis along local X)
 * @param semiAxisA semi-major axis (transverse axis)
 * @param semiAxisB semi-minor axis (conjugate axis)
 */
public record Hyperbola3(Axis2Placement3D position, double semiAxisA, double semiAxisB) implements Curve3 {

    /**
     * Creates a hyperbola and validates its invariants.
     */
    public Hyperbola3 {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(semiAxisA, "semiAxisA");
        Preconditions.requireFinite(semiAxisB, "semiAxisB");
        if (semiAxisA <= Epsilon.EPS || semiAxisB <= Epsilon.EPS) {
            throw new GeometryException("hyperbola semi-axes must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the hyperbola branch at a given parameter.
     * Parametric form: x = a*cos(t), y = b*sin(t) for hyperbolic cosine/sine
     * Using hyperbolic functions: x = a*sec(t), y = b*tan(t)
     * Or simpler: x = a*t, y = b*sqrt(t^2 - 1) for t >= 1
     *
     * @param t parameter value (t >= 1 or t <= -1 for the two branches)
     * @return point on the hyperbola (right branch for t > 0)
     */
    @Override
    public CartesianPoint pointAt(double t) {
        Preconditions.requireFinite(t, "t");
        // Parametric form using hyperbolic cosine and sine
        // Right branch: x = a*cosh(u), y = b*sinh(u) where u = arcosh(t)
        // For simpler sampling, use: x = a*|t|, y = b*sqrt(t^2 - 1)
        double absT = Math.abs(t);
        if (absT < 1.0) {
            absT = 1.0; // Clamp to minimum valid parameter
        }
        double x = semiAxisA * absT;
        double y = semiAxisB * Math.sqrt(absT * absT - 1.0);
        if (t < 0) {
            x = -x; // Left branch
        }

        // Transform to world coordinates
        Vector3 offset = position.xDirection().asVector().scale(x)
                .add(position.yDirection().asVector().scale(y));
        return position.location().add(offset);
    }

    /**
     * Samples one branch of the hyperbola at discrete points.
     *
     * @param segments number of segments
     * @param tMin minimum parameter value (>= 1)
     * @param tMax maximum parameter value
     * @param rightBranch true for right branch, false for left branch
     * @return list of sampled points
     */
    public List<CartesianPoint> sampleBranch(int segments, double tMin, double tMax, boolean rightBranch) {
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        double sign = rightBranch ? 1.0 : -1.0;
        for (int i = 0; i <= segments; i++) {
            double t = tMin + (tMax - tMin) * i / segments;
            points.add(pointAt(sign * t));
        }
        return List.copyOf(points);
    }

    /**
     * Samples both branches of the hyperbola.
     *
     * @param segments number of segments per branch
     * @return list of sampled points (left branch then right branch)
     */
    public List<CartesianPoint> sample(int segments) {
        List<CartesianPoint> points = new ArrayList<>();
        // Sample from t=1 to t=2 for each branch
        points.addAll(sampleBranch(segments, 1.0, 2.0, false));
        points.addAll(sampleBranch(segments, 1.0, 2.0, true));
        return List.copyOf(points);
    }

    /**
     * Computes the tangent vector at a given parameter on the hyperbola.
     *
     * @param t parameter value (t >= 1 for valid hyperbola points)
     * @return unit tangent vector
     */
    public Vector3 tangentAt(double t) {
        Preconditions.requireFinite(t, "t");
        double absT = Math.abs(t);
        // Use numerical differentiation to avoid singularity at t=1
        double eps = 1e-6;
        double t1 = Math.max(1.0 + eps, absT - eps);
        double t2 = absT + eps;

        CartesianPoint p1 = pointAt(Math.signum(t) * t1);
        CartesianPoint p2 = pointAt(Math.signum(t) * t2);
        Vector3 tangent = p2.subtract(p1);

        if (tangent.norm() <= Epsilon.EPS) {
            return position.xDirection().asVector();
        }
        return tangent.normalize().asVector();
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point lies on the hyperbola plane
        Vector3 offset = point.subtract(position.location());
        double planeDistance = Math.abs(offset.dot(position.axis().asVector()));
        if (planeDistance > Epsilon.EPS) {
            return false;
        }
        // Hyperbola equation in local coordinates: x^2/a^2 - y^2/b^2 = 1
        double x = offset.dot(position.xDirection().asVector());
        double y = offset.dot(position.yDirection().asVector());
        return Epsilon.equals(x * x / (semiAxisA * semiAxisA) - y * y / (semiAxisB * semiAxisB), 1.0);
    }

    /**
     * Returns the approximate bounding box for a parameter range.
     * Since hyperbolas are unbounded, this requires parameter bounds.
     *
     * @param tMin minimum parameter (>= 1)
     * @param tMax maximum parameter
     * @return bounding box for the parameter range
     */
    public BoundingBox3 boundingBox(double tMin, double tMax) {
        Preconditions.requireFinite(tMin, "tMin");
        Preconditions.requireFinite(tMax, "tMax");
        BoundingBox3 box = BoundingBox3.empty();
        int segments = Math.max(8, (int) Math.ceil(Math.abs(tMax - tMin) * 4));
        for (int i = 0; i <= segments; i++) {
            double t = tMin + (tMax - tMin) * i / segments;
            box = box.union(pointAt(t));
            box = box.union(pointAt(-t)); // Also include left branch
        }
        return box;
    }

    /**
     * Returns a default bounding box for visualization (t from 1 to 2).
     *
     * @return bounding box for default parameter range
     */
    public BoundingBox3 boundingBox() {
        return boundingBox(1.0, 2.0);
    }

    /**
     * Computes the curvature at a given parameter on the hyperbola.
     *
     * @param t parameter value (t >= 1 for valid hyperbola points)
     * @return curvature
     */
    public double curvatureAt(double t) {
        Preconditions.requireFinite(t, "t");
        double absT = Math.abs(t);
        if (absT < 1.0) {
            absT = 1.0;
        }
        // Curvature for hyperbola: k = (a*b) / (a^2*sinh^2(u) + b^2*cosh^2(u))^3/2
        // Using parametric form x = a*sec(u), y = b*tan(u)
        // Simplified approximation
        double a = semiAxisA;
        double b = semiAxisB;
        double denom = Math.pow(a * a * absT * absT + b * b * (absT * absT - 1), 1.5);
        if (denom <= Epsilon.EPS) {
            return 0.0;
        }
        return (a * b * absT) / denom;
    }

    /**
     * Returns the closest point on the hyperbola to a given point.
     *
     * @param point target point
     * @return closest point on the hyperbola (approximately)
     */
    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Project point onto hyperbola plane
        Vector3 offset = point.subtract(position.location());
        double planeDistance = offset.dot(position.axis().asVector());
        Vector3 axialOffset = position.axis().asVector().scale(planeDistance);
        CartesianPoint projected = new CartesianPoint(
            point.x() - axialOffset.x(),
            point.y() - axialOffset.y(),
            point.z() - axialOffset.z()
        );

        // Find approximate parameter from projected point
        Vector3 radialOffset = projected.subtract(position.location());
        double x = radialOffset.dot(position.xDirection().asVector());
        double y = radialOffset.dot(position.yDirection().asVector());

        // Solve for t approximately: x = a*|t|, y = b*sqrt(t^2-1)
        double sign = x >= 0 ? 1.0 : -1.0;
        double tApprox = Math.abs(x) / semiAxisA;
        if (tApprox < 1.0) {
            tApprox = 1.0;
        }

        return pointAt(sign * tApprox);
    }

    /**
     * Returns the distance from a point to the hyperbola.
     *
     * @param point target point
     * @return approximate distance to the hyperbola
     */
    @Override
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the semi-major axis (transverse axis).
     *
     * @return semi-major axis length
     */
    public double semiMajorAxis() {
        return semiAxisA;
    }

    /**
     * Returns the semi-minor axis (conjugate axis).
     *
     * @return semi-minor axis length
     */
    public double semiMinorAxis() {
        return semiAxisB;
    }

    /**
     * Returns the eccentricity of the hyperbola.
     *
     * @return eccentricity (>= 1)
     */
    public double eccentricity() {
        return Math.sqrt(1.0 + semiAxisB * semiAxisB / (semiAxisA * semiAxisA));
    }
}