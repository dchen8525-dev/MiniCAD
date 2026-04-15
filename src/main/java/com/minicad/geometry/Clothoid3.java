package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal 3D clothoid (Euler spiral / transition curve) representation.
 * The clothoid is defined by its curvature varying linearly with arc length.
 *
 * @param position clothoid placement (start point and local coordinate system)
 * @param xAxisIntercept x-coordinate where the clothoid intersects the x-axis
 * @param curvature curvature parameter (rate of curvature change per unit length)
 */
public record Clothoid3(Axis2Placement3D position, double xAxisIntercept, double curvature) implements Curve3 {

    /**
     * Creates a clothoid and validates its invariants.
     */
    public Clothoid3 {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(xAxisIntercept, "xAxisIntercept");
        Preconditions.requireFinite(curvature, "curvature");
        if (Math.abs(curvature) < Epsilon.EPS) {
            throw new GeometryException("clothoid curvature must be non-zero");
        }
    }

    /**
     * Evaluates a point on the clothoid at a given parameter value.
     * Uses Fresnel integrals for the Euler spiral.
     *
     * @param t parameter value (arc length scaled by curvature)
     * @return point on the clothoid
     */
    public CartesianPoint pointAt(double t) {
        Preconditions.requireFinite(t, "t");
        // Fresnel integrals: C(t) = integral of cos(pi*t^2/2), S(t) = integral of sin(pi*t^2/2)
        double scale = Math.sqrt(Math.PI / 2.0) * xAxisIntercept;
        double[] fresnel = fresnelIntegrals(t);
        double x = fresnel[0] * scale;
        double y = fresnel[1] * scale;

        // Transform to world coordinates
        Vector3 offset = position.xDirection().asVector().scale(x)
                .add(position.yDirection().asVector().scale(y));
        return position.location().add(offset);
    }

    /**
     * Computes Fresnel integrals C(t) and S(t) using numerical approximation.
     *
     * @param t parameter value
     * @return array with [C(t), S(t)]
     */
    private double[] fresnelIntegrals(double t) {
        // Use piecewise approximation for Fresnel integrals
        // For small t, use series expansion; for larger t, use asymptotic expansion
        if (Math.abs(t) < 0.5) {
            return fresnelSeries(t);
        } else {
            return fresnelAsymptotic(t);
        }
    }

    /**
     * Series expansion for Fresnel integrals (accurate for small t).
     */
    private double[] fresnelSeries(double t) {
        double t2 = t * t;
        double piHalf = Math.PI / 2.0;
        double c = 0.0;
        double s = 0.0;
        double term = t;
        int sign = 1;
        for (int n = 0; n < 20; n++) {
            double power = 2 * n + 1;
            double denom = power * factorial(power);
            if (n > 0) {
                term *= t2 * piHalf / (2.0 * n);
            }
            if (sign > 0) {
                c += term / denom;
            } else {
                s += term / denom;
            }
            sign = -sign;
        }
        return new double[]{c, s};
    }

    /**
     * Asymptotic expansion for Fresnel integrals (accurate for large t).
     */
    private double[] fresnelAsymptotic(double t) {
        double piHalf = Math.PI / 2.0;
        double t2 = t * t;
        double piT2 = piHalf * t2;
        double signC = Math.cos(piT2);
        double signS = Math.sin(piT2);
        double piT = Math.PI * t;
        // C(t) ~ 0.5 + (cos(pi*t^2) / (pi*t)) * (1 - 1/(pi^2*t^4) + ...)
        // S(t) ~ 0.5 + (sin(pi*t^2) / (pi*t)) * (1 - 1/(pi^2*t^4) + ...)
        double base = 1.0 / (piT);
        double correction = 1.0 / (piT * piT * piT * piT);
        double c = 0.5 + signC * base * (1.0 - correction / 3.0);
        double s = 0.5 + signS * base * (1.0 - correction);
        return new double[]{c, s};
    }

    /**
     * Computes factorial for small integers.
     */
    private double factorial(double n) {
        int ni = (int) n;
        double result = 1.0;
        for (int i = 2; i <= ni; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Samples the clothoid at discrete points.
     *
     * @param segments number of segments
     * @return list of sampled points
     */
    public List<CartesianPoint> sample(int segments) {
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        // Sample from t=0 to t corresponding to the clothoid length
        double maxT = 1.0; // Typical range for clothoid parameter
        for (int i = 0; i <= segments; i++) {
            double t = maxT * i / segments;
            points.add(pointAt(t));
        }
        return List.copyOf(points);
    }

    /**
     * Computes the tangent vector at a given parameter.
     * Uses numerical differentiation.
     *
     * @param t parameter value
     * @return unit tangent vector
     */
    public Vector3 tangentAt(double t) {
        Preconditions.requireFinite(t, "t");
        double eps = 1e-6;
        double t1 = Math.max(0, t - eps);
        double t2 = t + eps;

        CartesianPoint p1 = pointAt(t1);
        CartesianPoint p2 = pointAt(t2);
        Vector3 tangent = p2.subtract(p1);

        if (tangent.norm() <= Epsilon.EPS) {
            // At t=0, the tangent is along X direction
            return position.xDirection().asVector();
        }
        return tangent.normalize().asVector();
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point lies on the clothoid plane
        Vector3 offset = point.subtract(position.location());
        double planeDistance = Math.abs(offset.dot(position.axis().asVector()));
        return planeDistance <= Epsilon.EPS;
    }

    /**
     * Returns the approximate bounding box for a parameter range.
     * Since clothoids extend infinitely, this requires parameter bounds.
     *
     * @param tMin minimum parameter
     * @param tMax maximum parameter
     * @return bounding box for the parameter range
     */
    public BoundingBox3 boundingBox(double tMin, double tMax) {
        Preconditions.requireFinite(tMin, "tMin");
        Preconditions.requireFinite(tMax, "tMax");
        BoundingBox3 box = BoundingBox3.empty();
        int segments = Math.max(8, (int) Math.ceil(Math.abs(tMax - tMin) * 8));
        for (int i = 0; i <= segments; i++) {
            double t = tMin + (tMax - tMin) * i / segments;
            box = box.union(pointAt(t));
        }
        return box;
    }

    /**
     * Returns a default bounding box for visualization.
     *
     * @return bounding box for default parameter range
     */
    public BoundingBox3 boundingBox() {
        return boundingBox(0.0, 1.0);
    }

    /**
     * Returns the approximate length of the clothoid segment.
     *
     * @return approximate length
     */
    public double length() {
        java.util.List<CartesianPoint> samples = sample(256);
        double totalLength = 0.0;
        for (int i = 0; i < samples.size() - 1; i++) {
            totalLength += samples.get(i).distanceTo(samples.get(i + 1));
        }
        return totalLength;
    }

    /**
     * Returns the closest point on the clothoid to a given point.
     *
     * @param point target point
     * @return closest point on the clothoid (approximately)
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Project point onto clothoid plane
        Vector3 offset = point.subtract(position.location());
        double planeDistance = offset.dot(position.axis().asVector());
        Vector3 axialOffset = position.axis().asVector().scale(planeDistance);
        CartesianPoint projected = new CartesianPoint(
            point.x() - axialOffset.x(),
            point.y() - axialOffset.y(),
            point.z() - axialOffset.z()
        );

        // Find closest by sampling
        java.util.List<CartesianPoint> samples = sample(256);
        CartesianPoint closest = samples.get(0);
        double minDistance = projected.distanceTo(closest);
        for (int i = 1; i < samples.size(); i++) {
            double distance = projected.distanceTo(samples.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                closest = samples.get(i);
            }
        }
        return closest;
    }

    /**
     * Returns the distance from a point to the clothoid.
     *
     * @param point target point
     * @return approximate distance to the clothoid
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the curvature rate parameter.
     *
     * @return curvature parameter
     */
    public double curvatureRate() {
        return curvature;
    }

    /**
     * Returns the x-axis intercept parameter.
     *
     * @return x-axis intercept
     */
    public double intercept() {
        return xAxisIntercept;
    }
}