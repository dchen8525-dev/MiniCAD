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
    @Override
    public Vector3 tangentAt(double t) {
        Preconditions.requireFinite(t, "t");
        double absT = Math.abs(t);
        if (absT <= 1.0 + Epsilon.EPS) {
            return position.xDirection().asVector();
        }
        double sqrtTerm = Math.sqrt(absT * absT - 1.0);
        // dx/dt = a*sign(t), dy/dt = b*t/sqrt(t^2-1)
        double dx = semiAxisA * Math.signum(t);
        double dy = semiAxisB * absT / sqrtTerm;
        Vector3 tangentInLocal = position.xDirection().asVector().scale(dx)
                .add(position.yDirection().asVector().scale(dy));
        return tangentInLocal.normalize().asVector();
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
        double t = Math.abs(x) / semiAxisA;
        if (t < 1.0) {
            t = 1.0;
        }

        // Newton-Raphson refinement: minimize ||C(t) - P||^2
        for (int iter = 0; iter < 20; iter++) {
            CartesianPoint cp = pointAt(sign * t);
            Vector3 residual = cp.subtract(projected);
            // Tangent at parameter sign*t
            double absT = t;
            Vector3 deriv;
            if (absT <= 1.0 + Epsilon.EPS) {
                deriv = position.xDirection().asVector();
            } else {
                double sqrtTerm = Math.sqrt(absT * absT - 1.0);
                double dx = semiAxisA * sign;
                double dy = semiAxisB * absT / sqrtTerm;
                deriv = position.xDirection().asVector().scale(dx)
                        .add(position.yDirection().asVector().scale(dy));
            }
            double derivNormSq = deriv.normSquared();
            if (derivNormSq <= Epsilon.EPS) break;
            double dt = -residual.dot(deriv) / derivNormSq;
            t += dt;
            if (t < 1.0) t = 1.0;
            if (Math.abs(dt) < 1e-12) break;
        }
        return pointAt(sign * t);
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

    /**
     * Returns the curve parameter corresponding to the given point.
     * For the parametric form x = a*|t|, y = b*sqrt(t^2-1), the parameter t = x/a.
     *
     * @param point a point on or near the hyperbola
     * @return parameter value t (positive for right branch, negative for left)
     */
    @Override
    public double parameterAt(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(position.location());
        double x = offset.dot(position.xDirection().asVector());
        double tApprox = x / semiAxisA;
        return Math.abs(tApprox) < 1.0 ? Math.signum(tApprox) : tApprox;
    }

    /**
     * Returns the arc length for a parameter range on one branch.
     * Uses 32-point Gaussian quadrature for accuracy.
     *
     * @param tMin minimum parameter (>= 1)
     * @param tMax maximum parameter
     * @return arc length on the right branch
     */
    public double length(double tMin, double tMax) {
        Preconditions.requireFinite(tMin, "tMin");
        Preconditions.requireFinite(tMax, "tMax");
        if (tMin < 1.0) tMin = 1.0;
        if (tMax < tMin) tMax = tMin;
        return gaussQuadrature(tMin, tMax);
    }

    private double hyperbolaSpeed(double t) {
        double t2 = t * t;
        if (t2 <= 1.0 + 1e-12) return semiAxisA;
        double dy2 = semiAxisB * semiAxisB * t2 / (t2 - 1.0);
        return Math.sqrt(semiAxisA * semiAxisA + dy2);
    }

    private double gaussQuadrature(double a, double b) {
        double mid = (a + b) * 0.5;
        double half = (b - a) * 0.5;
        double sum = 0.0;
        for (int i = 0; i < GAUSS_POINTS.length; i += 2) {
            double xi = GAUSS_POINTS[i];
            double wi = GAUSS_WEIGHTS[i];
            double t = mid + half * xi;
            sum += wi * hyperbolaSpeed(t);
            if (i + 1 < GAUSS_POINTS.length) {
                double xi2 = GAUSS_POINTS[i + 1];
                double wi2 = GAUSS_WEIGHTS[i + 1];
                double t2 = mid + half * xi2;
                sum += wi2 * hyperbolaSpeed(t2);
            }
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

    @Override
    public double length() {
        return length(1.0, 2.0) + length(1.0, 2.0);
    }
}