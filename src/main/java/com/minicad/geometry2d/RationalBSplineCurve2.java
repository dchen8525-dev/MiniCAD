package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal rational 2D B-spline curve with knot multiplicities.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param weights weights for control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public record RationalBSplineCurve2(
        int degree,
        List<Point2> controlPoints,
        List<Double> weights,
        List<Integer> knotMultiplicities,
        List<Double> knots
) implements Curve2 {

    public RationalBSplineCurve2 {
        if (degree < 1) {
            throw new GeometryException("degree must be at least 1");
        }
        controlPoints = List.copyOf(controlPoints);
        weights = List.copyOf(weights);
        knotMultiplicities = List.copyOf(knotMultiplicities);
        knots = List.copyOf(knots);
        if (controlPoints.size() < degree + 1) {
            throw new GeometryException("control point count must be at least degree + 1");
        }
        if (weights.size() != controlPoints.size()) {
            throw new GeometryException("weights must match control points");
        }
        if (knotMultiplicities.size() != knots.size()) {
            throw new GeometryException("knot multiplicities and knots must have the same size");
        }
    }

    public List<Double> expandedKnots() {
        List<Double> expanded = new ArrayList<>();
        for (int index = 0; index < knots.size(); index++) {
            for (int repeat = 0; repeat < knotMultiplicities.get(index); repeat++) {
                expanded.add(knots.get(index));
            }
        }
        return List.copyOf(expanded);
    }

    public double startParameter() {
        List<Double> expanded = expandedKnots();
        return expanded.get(degree);
    }

    public double endParameter() {
        List<Double> expanded = expandedKnots();
        return expanded.get(expanded.size() - degree - 1);
    }

    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        List<Double> expanded = expandedKnots();
        int n = controlPoints.size() - 1;
        int p = degree;
        double low = expanded.get(p);
        double high = expanded.get(n + 1);
        double u = Math.max(low, Math.min(parameter, high));
        if (Epsilon.equals(u, high)) {
            return controlPoints.getLast();
        }
        int span = findSpan(n, p, u, expanded);
        double denominator = 0.0;
        Vector2 numerator = new Vector2(0.0, 0.0);
        for (int i = 0; i <= p; i++) {
            int controlIndex = span - p + i;
            double basis = basisValue(controlIndex, p, u, expanded);
            double weightedBasis = basis * weights.get(controlIndex);
            Point2 control = controlPoints.get(controlIndex);
            numerator = numerator.add(new Vector2(
                    control.x() * weightedBasis,
                    control.y() * weightedBasis
            ));
            denominator += weightedBasis;
        }
        if (Epsilon.isZero(denominator)) {
            throw new GeometryException("rational curve denominator is zero");
        }
        return new Point2(numerator.x() / denominator, numerator.y() / denominator);
    }

    public List<Point2> sample(int segments) {
        int effectiveSegments = Math.max(segments, 8);
        double start = startParameter();
        double end = endParameter();
        List<Point2> samples = new ArrayList<>(effectiveSegments + 1);
        for (int index = 0; index <= effectiveSegments; index++) {
            double u = start + (end - start) * index / effectiveSegments;
            samples.add(pointAt(u));
        }
        return List.copyOf(samples);
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        for (Point2 sample : sample(256)) {
            if (sample.subtract(point).norm() <= 1.0e-6) {
                return true;
            }
        }
        return false;
    }

    private static int findSpan(int n, int p, double u, List<Double> knots) {
        if (u >= knots.get(n + 1)) {
            return n;
        }
        int low = p;
        int high = n + 1;
        int mid = (low + high) / 2;
        while (u < knots.get(mid) || u >= knots.get(mid + 1)) {
            if (u < knots.get(mid)) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (low + high) / 2;
        }
        return mid;
    }

    private static double basisValue(int i, int degree, double parameter, List<Double> knots) {
        if (degree == 0) {
            if ((parameter >= knots.get(i) && parameter < knots.get(i + 1))
                    || (Epsilon.equals(parameter, knots.getLast()) && Epsilon.equals(parameter, knots.get(i + 1)))) {
                return 1.0;
            }
            return 0.0;
        }
        double leftDenominator = knots.get(i + degree) - knots.get(i);
        double rightDenominator = knots.get(i + degree + 1) - knots.get(i + 1);
        double left = Epsilon.isZero(leftDenominator)
                ? 0.0
                : (parameter - knots.get(i)) / leftDenominator * basisValue(i, degree - 1, parameter, knots);
        double right = Epsilon.isZero(rightDenominator)
                ? 0.0
                : (knots.get(i + degree + 1) - parameter) / rightDenominator * basisValue(i + 1, degree - 1, parameter, knots);
        return left + right;
    }

    /**
     * Computes the tangent vector at a given parameter.
     * Uses analytical rational B-spline derivative via quotient rule.
     *
     * @param parameter parameter in the knot domain
     * @return unit tangent vector
     */
    @Override
    public Vector2 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        List<Double> expanded = expandedKnots();
        int n = controlPoints.size() - 1;
        int p = degree;
        double low = expanded.get(p);
        double high = expanded.get(n + 1);
        double u = Math.max(low, Math.min(parameter, high));
        if (Epsilon.equals(u, high)) {
            u = high - 1e-12 * (high - low);
        }
        if (u <= low) {
            u = low + 1e-12 * (high - low);
        }

        // Rational curve: C(u) = A(u) / W(u)
        // C'(u) = (A'(u)*W(u) - A(u)*W'(u)) / W(u)^2
        Vector2 aPrime = new Vector2(0, 0);
        Vector2 a = new Vector2(0, 0);
        double wPrime = 0.0;
        double w = 0.0;

        for (int i = 0; i < controlPoints.size(); i++) {
            double Ni = basisValue(i, p, u, expanded);
            double dNi = derivativeBasisValue(i, p, u, expanded);
            double wi = weights.get(i);
            Point2 cp = controlPoints.get(i);

            a = a.add(new Vector2(cp.x() * wi * Ni, cp.y() * wi * Ni));
            w += wi * Ni;
            aPrime = aPrime.add(new Vector2(cp.x() * wi * dNi, cp.y() * wi * dNi));
            wPrime += wi * dNi;
        }

        Vector2 numerator = aPrime.scale(w).subtract(a.scale(wPrime));
        double denomSq = w * w;
        if (Epsilon.isZero(denomSq)) {
            return new Vector2(1, 0);
        }

        double norm = numerator.norm();
        if (norm <= Epsilon.EPS) {
            return new Vector2(1, 0);
        }
        return numerator.normalize().asVector();
    }

    /**
     * Computes the derivative of the i-th B-spline basis function of degree p at parameter u.
     */
    private static double derivativeBasisValue(int i, int degree, double u, List<Double> knots) {
        double left = 0.0;
        double leftDenom = knots.get(i + degree) - knots.get(i);
        if (!Epsilon.isZero(leftDenom)) {
            left = degree / leftDenom * basisValue(i, degree - 1, u, knots);
        }
        double right = 0.0;
        double rightDenom = knots.get(i + degree + 1) - knots.get(i + 1);
        if (!Epsilon.isZero(rightDenom)) {
            right = degree / rightDenom * basisValue(i + 1, degree - 1, u, knots);
        }
        return left - right;
    }

    /**
     * Returns the closest point on the curve to a given point.
     * Uses sampling for initial guess then Newton-Raphson refinement.
     *
     * @param point target point
     * @return closest point on the curve
     */
    @Override
    public Point2 closestPointTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        double start = startParameter();
        double end = endParameter();
        List<Point2> samples = sample(256);
        int bestIndex = 0;
        double minDistance = point.distanceTo(samples.get(0));
        for (int i = 1; i < samples.size(); i++) {
            double distance = point.distanceTo(samples.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                bestIndex = i;
            }
        }
        double t = start + (end - start) * bestIndex / (samples.size() - 1);
        // Newton-Raphson refinement: minimize ||C(t) - P||^2
        for (int iter = 0; iter < 20; iter++) {
            Point2 cp = pointAt(t);
            Vector2 residual = cp.subtract(point);
            Vector2 deriv = tangentAt(t);
            double derivNormSq = deriv.normSquared();
            if (derivNormSq <= Epsilon.EPS) break;
            double dt = -residual.dot(deriv) / derivNormSq;
            t = Math.max(start, Math.min(end, t + dt));
            if (Math.abs(dt) < 1e-12) break;
        }
        return pointAt(t);
    }

    /**
     * Returns the arc length of the curve using 32-point Gaussian quadrature.
     *
     * @return arc length over the full parameter domain
     */
    @Override
    public double length() {
        return gaussQuadrature(startParameter(), endParameter());
    }

    private static final int GAUSS_N = 32;
    private static final double[] GAUSS_X = {
        -0.997280250405579, -0.986877945300497, -0.966778744689954, -0.937273392400729,
        -0.898756597811589, -0.851971559315161, -0.797881950658778, -0.737558060150557,
        -0.672151931853181, -0.602874568325758, -0.530995029162380, -0.457807938710424,
        -0.384646206394776, -0.312815855597286, -0.243623670801664, -0.178330365806909,
         0.178330365806909,  0.243623670801664,  0.312815855597286,  0.384646206394776,
         0.457807938710424,  0.530995029162380,  0.602874568325758,  0.672151931853181,
         0.737558060150557,  0.797881950658778,  0.851971559315161,  0.898756597811589,
         0.937273392400729,  0.966778744689954,  0.986877945300497,  0.997280250405579
    };
    private static final double[] GAUSS_W = {
        0.007018610009469, 0.016274394730906, 0.025392065309262, 0.034273862913022,
        0.042835898022226, 0.050998059262376, 0.058684093478536, 0.065822222776362,
        0.072345794108848, 0.078193895787070, 0.083311924226947, 0.087652093004404,
        0.091173878695764, 0.093844399080805, 0.095638720079275, 0.096540088514728,
        0.096540088514728, 0.095638720079275, 0.093844399080805, 0.091173878695764,
        0.087652093004404,  0.083311924226947,  0.078193895787070,  0.072345794108848,
        0.065822222776362,  0.058684093478536,  0.050998059262376,  0.042835898022226,
        0.034273862913022,  0.025392065309262,  0.016274394730906,  0.007018610009469
    };

    private double gaussQuadrature(double a, double b) {
        double mid = (a + b) * 0.5;
        double halfLen = (b - a) * 0.5;
        double sum = 0.0;
        for (int i = 0; i < GAUSS_N; i++) {
            double t = mid + halfLen * GAUSS_X[i];
            double speed = derivativeNorm(t);
            sum += GAUSS_W[i] * speed;
        }
        return sum * halfLen;
    }

    private double derivativeNorm(double u) {
        List<Double> expanded = expandedKnots();
        int n = controlPoints.size() - 1;
        int p = degree;
        // Rational B-spline derivative: C'(u) = (A'(u)*W(u) - A(u)*W'(u)) / W(u)^2
        Vector2 aPrim = new Vector2(0, 0);
        double wPrim = 0.0;
        for (int i = 0; i <= n; i++) {
            double dN = derivativeBasisValue(i, p, u, expanded);
            if (dN == 0.0) continue;
            Point2 cp = controlPoints.get(i);
            aPrim = aPrim.add(new Vector2(cp.x() * dN, cp.y() * dN));
            wPrim += weights.get(i) * dN;
        }
        Point2 rationalPt = rationalPointAt(u);
        double w = weightAt(u);
        Vector2 a = new Vector2(rationalPt.x(), rationalPt.y()).scale(w);
        double wSq = w * w;
        if (wSq <= Epsilon.EPS) return 0.0;
        Vector2 deriv = aPrim.scale(w).subtract(a.scale(wPrim)).scale(1.0 / wSq);
        return deriv.norm();
    }

    private double weightAt(double u) {
        List<Double> expanded = expandedKnots();
        int n = controlPoints.size() - 1;
        int p = degree;
        double w = 0.0;
        for (int i = 0; i <= n; i++) {
            w += weights.get(i) * basisValue(i, p, u, expanded);
        }
        return w;
    }

    private Point2 rationalPointAt(double u) {
        List<Double> expanded = expandedKnots();
        int n = controlPoints.size() - 1;
        int p = degree;
        double wx = 0, wy = 0, w = 0;
        for (int i = 0; i <= n; i++) {
            double b = basisValue(i, p, u, expanded) * weights.get(i);
            Point2 cp = controlPoints.get(i);
            wx += cp.x() * b;
            wy += cp.y() * b;
            w += b;
        }
        if (w == 0.0) return controlPoints.get(0);
        return new Point2(wx / w, wy / w);
    }

    /**
     * Returns the arc length of the curve over a sub-interval.
     *
     * @param tMin start parameter
     * @param tMax end parameter
     * @return arc length from tMin to tMax
     */
    public double length(double tMin, double tMax) {
        Preconditions.requireFinite(tMin, "tMin");
        Preconditions.requireFinite(tMax, "tMax");
        return gaussQuadrature(tMin, tMax);
    }

    /**
     * Returns the approximate bounding box based on control points.
     *
     * @return bounding box enclosing all control points
     */
    public BoundingBox2 boundingBox() {
        return BoundingBox2.of(controlPoints);
    }

    /**
     * Returns a more accurate bounding box by sampling the curve.
     *
     * @param segments number of segments to sample
     * @return bounding box enclosing sampled curve points
     */
    public BoundingBox2 boundingBox(int segments) {
        return BoundingBox2.of(sample(segments));
    }

    /**
     * Returns the distance from a point to the curve.
     *
     * @param point target point
     * @return minimum distance to the curve
     */
    public double distanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the midpoint of the curve.
     *
     * @return midpoint
     */
    public Point2 midpoint() {
        double midParam = (startParameter() + endParameter()) / 2.0;
        return pointAt(midParam);
    }

    /**
     * Returns the control point count.
     *
     * @return number of control points
     */
    public int controlPointCount() {
        return controlPoints.size();
    }

    /**
     * Returns the knot count (unique knots).
     *
     * @return number of unique knots
     */
    public int knotCount() {
        return knots.size();
    }

    /**
     * Returns the weight count.
     *
     * @return number of weights
     */
    public int weightCount() {
        return weights.size();
    }
}
