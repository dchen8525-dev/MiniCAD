package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal 2D hyperbola representation.
 * A hyperbola is a conic section defined by two semi-axes.
 *
 * @param center hyperbola center
 * @param xDirection local x direction (transverse axis direction)
 * @param semiAxisA semi-major axis (transverse axis)
 * @param semiAxisB semi-minor axis (conjugate axis)
 */
public record Hyperbola2(Point2 center, Direction2 xDirection, double semiAxisA, double semiAxisB) implements Curve2 {

    /**
     * Creates a hyperbola and validates its invariants.
     */
    public Hyperbola2 {
        Preconditions.requireNonNull(center, "center");
        Preconditions.requireNonNull(xDirection, "xDirection");
        Preconditions.requireFinite(semiAxisA, "semiAxisA");
        Preconditions.requireFinite(semiAxisB, "semiAxisB");
        if (semiAxisA <= Epsilon.EPS || semiAxisB <= Epsilon.EPS) {
            throw new GeometryException("hyperbola semi-axes must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the hyperbola branch at a given parameter.
     * Parametric form using hyperbolic functions: x = a*cosh(t), y = b*sinh(t)
     *
     * @param t parameter value
     * @param rightBranch true for right branch, false for left branch
     * @return point on the hyperbola
     */
    public Point2 pointAt(double t, boolean rightBranch) {
        Preconditions.requireFinite(t, "t");
        double sign = rightBranch ? 1.0 : -1.0;
        double x = semiAxisA * Math.cosh(t) * sign;
        double y = semiAxisB * Math.sinh(t);

        Vector2 xAxis = xDirection.asVector();
        Vector2 yAxis = new Vector2(-xAxis.y(), xAxis.x());
        return center.add(xAxis.scale(x).add(yAxis.scale(y)));
    }

    /**
     * Evaluates a point on the right branch of the hyperbola at a given parameter.
     *
     * @param t parameter value
     * @return point on the right branch
     */
    public Point2 pointAt(double t) {
        return pointAt(t, true);
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 offset = point.subtract(center);
        Vector2 xAxis = xDirection.asVector();
        Vector2 yAxis = new Vector2(-xAxis.y(), xAxis.x());
        double x = offset.dot(xAxis);
        double y = offset.dot(yAxis);
        // Hyperbola equation: x^2/a^2 - y^2/b^2 = 1
        return Epsilon.equals(x * x / (semiAxisA * semiAxisA) - y * y / (semiAxisB * semiAxisB), 1.0);
    }

    /**
     * Samples one branch of the hyperbola at discrete points.
     *
     * @param segments number of segments
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @param rightBranch true for right branch, false for left branch
     * @return list of sampled points
     */
    public List<Point2> sampleBranch(int segments, double tMin, double tMax, boolean rightBranch) {
        List<Point2> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double t = tMin + (tMax - tMin) * i / segments;
            points.add(pointAt(t, rightBranch));
        }
        return List.copyOf(points);
    }

    /**
     * Samples the right branch of the hyperbola from t=0 to t=1.
     *
     * @param segments number of segments
     * @return list of sampled points
     */
    public List<Point2> sample(int segments) {
        return sample(segments, 0.0, 1.0, true);
    }

    /**
     * Samples the hyperbola at discrete points with parameter range.
     *
     * @param segments number of segments
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @param rightBranch true for right branch, false for left branch
     * @return list of sampled points
     */
    public List<Point2> sample(int segments, double tMin, double tMax, boolean rightBranch) {
        return sampleBranch(segments, tMin, tMax, rightBranch);
    }

    /**
     * Samples both branches of the hyperbola.
     *
     * @param segments number of segments per branch
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @return list of sampled points (left branch then right branch)
     */
    public List<Point2> sampleBothBranches(int segments, double tMin, double tMax) {
        List<Point2> points = new ArrayList<>();
        points.addAll(sampleBranch(segments, tMin, tMax, false));
        points.addAll(sampleBranch(segments, tMin, tMax, true));
        return List.copyOf(points);
    }

    /**
     * Computes the tangent vector at a given parameter.
     *
     * @param t parameter value
     * @param rightBranch true for right branch, false for left branch
     * @return unit tangent vector
     */
    public Vector2 tangentAt(double t, boolean rightBranch) {
        Preconditions.requireFinite(t, "t");
        double sign = rightBranch ? 1.0 : -1.0;
        // Derivative: x = a*sinh(t)*sign, y = b*cosh(t)
        double dx = semiAxisA * Math.sinh(t) * sign;
        double dy = semiAxisB * Math.cosh(t);

        Vector2 xAxis = xDirection.asVector();
        Vector2 yAxis = new Vector2(-xAxis.y(), xAxis.x());
        Vector2 tangent = xAxis.scale(dx).add(yAxis.scale(dy));
        if (tangent.norm() <= Epsilon.EPS) {
            return xAxis;
        }
        return tangent.normalize().asVector();
    }

    /**
     * Computes the tangent vector on the right branch at a given parameter.
     *
     * @param t parameter value
     * @return unit tangent vector
     */
    public Vector2 tangentAt(double t) {
        return tangentAt(t, true);
    }

    /**
     * Computes the normal vector at a given parameter.
     * The normal is perpendicular to the tangent.
     *
     * @param t parameter value
     * @param rightBranch true for right branch, false for left branch
     * @return unit normal vector
     */
    public Vector2 normalAt(double t, boolean rightBranch) {
        Vector2 tangent = tangentAt(t, rightBranch);
        // Rotate tangent 90 degrees
        return new Vector2(-tangent.y(), tangent.x());
    }

    /**
     * Computes the curvature at a given parameter.
     * Curvature of hyperbola: |a*b| / (a^2*sinh^2(t) + b^2*cosh^2(t))^1.5
     *
     * @param t parameter value
     * @return curvature value
     */
    public double curvatureAt(double t) {
        Preconditions.requireFinite(t, "t");
        double sinhT = Math.sinh(t);
        double coshT = Math.cosh(t);
        double denominator = Math.pow(semiAxisA * semiAxisA * sinhT * sinhT + semiAxisB * semiAxisB * coshT * coshT, 1.5);
        if (Epsilon.isZero(denominator)) {
            return 0.0;
        }
        return semiAxisA * semiAxisB / denominator;
    }

    /**
     * Returns the parameter value for the given point projected onto the hyperbola.
     * The parameter t is derived from the y component: t = arcsinh(y/b).
     * Always returns the parameter on the right branch.
     *
     * @param point point to project
     * @return parameter value of closest point on the right branch
     */
    public double parameterOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 offset = point.subtract(center);
        Vector2 xAxis = xDirection.asVector();
        Vector2 yAxis = new Vector2(-xAxis.y(), xAxis.x());
        double y = offset.dot(yAxis);
        return Math.log(y / semiAxisB + Math.sqrt(y * y / (semiAxisB * semiAxisB) + 1.0));
    }

    /**
     * Returns the approximate bounding box for a branch of the hyperbola.
     *
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @param rightBranch true for right branch, false for left branch
     * @return bounding box enclosing the sampled branch
     */
    public BoundingBox2 boundingBox(double tMin, double tMax, boolean rightBranch) {
        return BoundingBox2.of(sampleBranch(32, tMin, tMax, rightBranch));
    }

    /**
     * Returns the bounding box enclosing the default sampling range.
     *
     * @return bounding box enclosing the hyperbola
     */
    public BoundingBox2 boundingBox() {
        return boundingBox(0.0, 1.0, true);
    }

    /**
     * Returns the closest point on a branch of the hyperbola to a given point.
     * Uses sampling for initial guess then Newton-Raphson refinement.
     *
     * @param point target point
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @param rightBranch true for right branch, false for left branch
     * @return closest point on the hyperbola branch
     */
    public Point2 closestPointTo(Point2 point, double tMin, double tMax, boolean rightBranch) {
        Preconditions.requireNonNull(point, "point");
        Vector2 xAxis = xDirection.asVector();
        Vector2 yAxis = new Vector2(-xAxis.y(), xAxis.x());
        double sign = rightBranch ? 1.0 : -1.0;

        // Find initial guess by sampling
        List<Point2> samples = sampleBranch(64, tMin, tMax, rightBranch);
        double bestT = tMin;
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < samples.size(); i++) {
            double distance = point.distanceTo(samples.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                bestT = tMin + (tMax - tMin) * i / (samples.size() - 1);
            }
        }

        // Newton-Raphson refinement: minimize ||C(t) - P||^2
        double t = bestT;
        for (int iter = 0; iter < 20; iter++) {
            Point2 cp = pointAt(t, rightBranch);
            Vector2 residual = cp.subtract(point);
            // Tangent: C'(t) = a*sinh(t)*sign*xAxis + b*cosh(t)*yAxis
            double dx = semiAxisA * Math.sinh(t) * sign;
            double dy = semiAxisB * Math.cosh(t);
            Vector2 deriv = xAxis.scale(dx).add(yAxis.scale(dy));
            double derivNormSq = deriv.normSquared();
            if (derivNormSq <= Epsilon.EPS) break;
            double dt = -residual.dot(deriv) / derivNormSq;
            t += dt;
            if (Math.abs(dt) < 1e-12) break;
        }
        return pointAt(t, rightBranch);
    }

    /**
     * Returns the closest point on the right branch to a given point.
     *
     * @param point target point
     * @return closest point on the right branch
     */
    public Point2 closestPointTo(Point2 point) {
        return closestPointTo(point, 0.0, 1.0, true);
    }

    /**
     * Returns the distance from a point to the right branch.
     *
     * @param point target point
     * @return minimum distance to the right branch
     */
    public double distanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the length of a branch segment.
     * Uses 32-point Gaussian quadrature for accuracy.
     *
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @param rightBranch true for right branch, false for left branch
     * @return length of the segment
     */
    public double length(double tMin, double tMax, boolean rightBranch) {
        Preconditions.requireFinite(tMin, "tMin");
        Preconditions.requireFinite(tMax, "tMax");
        return gaussQuadrature(tMin, tMax);
    }

    private double hyperbolaSpeed(double t) {
        double sinhT = Math.sinh(t);
        double coshT = Math.cosh(t);
        return Math.sqrt(semiAxisA * semiAxisA * sinhT * sinhT + semiAxisB * semiAxisB * coshT * coshT);
    }

    private double gaussQuadrature(double a, double b) {
        double mid = (a + b) * 0.5;
        double half = (b - a) * 0.5;
        double sum = 0.0;
        for (int i = 0; i < GAUSS_POINTS.length; i++) {
            double t = mid + half * GAUSS_POINTS[i];
            sum += GAUSS_WEIGHTS[i] * hyperbolaSpeed(t);
        }
        return sum * half;
    }

    private static final double[] GAUSS_POINTS = {
        -0.991821584, -0.968158569, -0.919962775, -0.849122978, -0.757785470,
        -0.648606984, -0.524847769, -0.390250460, -0.248715320, -0.104097113,
         0.104097113,  0.248715320,  0.390250460,  0.524847769,  0.648606984,
         0.757785470,  0.849122978,  0.919962775,  0.968158569,  0.991821584
    };
    private static final double[] GAUSS_WEIGHTS = {
        0.020702387,  0.020520775,  0.020150043,  0.019593433,  0.018856867,
        0.017948300,  0.016877522,  0.015655979,  0.014296616,  0.012813781,
        0.012813781,  0.014296616,  0.015655979,  0.016877522,  0.017948300,
        0.018856867,  0.019593433,  0.020150043,  0.020520775,  0.020702387
    };

    /**
     * Returns the length of the right branch segment.
     *
     * @return approximate length
     */
    public double length() {
        return length(0.0, 1.0, true);
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
     * Eccentricity e = sqrt(1 + b^2/a^2)
     *
     * @return eccentricity value
     */
    public double eccentricity() {
        return Math.sqrt(1.0 + semiAxisB * semiAxisB / (semiAxisA * semiAxisA));
    }

    /**
     * Returns the foci of the hyperbola.
     * Focus positions are at (c, 0) and (-c, 0) relative to center, where c = sqrt(a^2 + b^2).
     *
     * @return list of focus points
     */
    public List<Point2> foci() {
        double c = Math.sqrt(semiAxisA * semiAxisA + semiAxisB * semiAxisB);
        Vector2 xAxis = xDirection.asVector();
        Point2 focus1 = center.add(xAxis.scale(c));
        Point2 focus2 = center.subtract(xAxis.scale(c));
        return List.of(focus1, focus2);
    }

    /**
     * Returns the y direction (perpendicular to x direction).
     *
     * @return y direction
     */
    public Direction2 yDirection() {
        Vector2 xAxis = xDirection.asVector();
        return Direction2.from(new Vector2(-xAxis.y(), xAxis.x()));
    }

    /**
     * Creates a hyperbola aligned with the x-axis.
     *
     * @param center hyperbola center
     * @param semiAxisA semi-major axis
     * @param semiAxisB semi-minor axis
     * @return hyperbola aligned with x-axis
     */
    public static Hyperbola2 at(Point2 center, double semiAxisA, double semiAxisB) {
        return new Hyperbola2(center, Direction2.from(new Vector2(1, 0)), semiAxisA, semiAxisB);
    }
}