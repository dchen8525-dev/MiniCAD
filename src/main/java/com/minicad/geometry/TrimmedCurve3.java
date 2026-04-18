package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Minimal trimmed-curve wrapper over a supported basis curve.
 * Trims are stored as parameter values on the basis curve; the geometric
 * trim endpoints are derived by evaluating the basis curve at those parameters.
 *
 * @param basisCurve supported basis curve
 * @param trimParamStart parameter value for the first trim
 * @param trimParamEnd parameter value for the second trim
 * @param senseAgreement trimming orientation agreement
 */
public record TrimmedCurve3(
        Curve3 basisCurve,
        double trimParamStart,
        double trimParamEnd,
        boolean senseAgreement
) implements Curve3 {

    /**
     * Creates a trimmed curve.
     */
    public TrimmedCurve3 {
        Preconditions.requireNonNull(basisCurve, "basisCurve");
        Preconditions.requireFinite(trimParamStart, "trimParamStart");
        Preconditions.requireFinite(trimParamEnd, "trimParamEnd");
    }

    /**
     * Returns the geometric trim start point.
     */
    public CartesianPoint trimStart() {
        return basisCurve.pointAt(trimParamStart);
    }

    /**
     * Returns the geometric trim end point.
     */
    public CartesianPoint trimEnd() {
        return basisCurve.pointAt(trimParamEnd);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Find closest point on the trimmed segment
        CartesianPoint closest = closestPointTo(point);
        if (point.distanceTo(closest) > Epsilon.EPS * 10) {
            return false;
        }
        // Find the parameter of the closest point on the basis curve
        double param = basisCurve.parameterAt(closest);
        double minP = Math.min(trimParamStart, trimParamEnd);
        double maxP = Math.max(trimParamStart, trimParamEnd);
        return param >= minP - Epsilon.EPS && param <= maxP + Epsilon.EPS;
    }

    /**
     * Evaluates a point on the trimmed curve at the given parameter.
     * The parameter is linearly mapped from [0,1] to [trimParamStart, trimParamEnd].
     *
     * @param parameter parameter value (0 to 1)
     * @return point on the basis curve at the mapped parameter
     */
    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        double mapped = senseAgreement
                ? trimParamStart + (trimParamEnd - trimParamStart) * parameter
                : trimParamEnd + (trimParamStart - trimParamEnd) * parameter;
        return basisCurve.pointAt(mapped);
    }

    /**
     * Samples the trimmed curve by sampling the basis curve between trim parameters.
     *
     * @param segments number of segments to sample
     * @return list of sampled points
     */
    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            double mapped = senseAgreement
                    ? trimParamStart + (trimParamEnd - trimParamStart) * t
                    : trimParamEnd + (trimParamStart - trimParamEnd) * t;
            points.add(basisCurve.pointAt(mapped));
        }
        return java.util.List.copyOf(points);
    }

    /**
     * Computes the tangent vector at a given parameter.
     *
     * @param parameter parameter value (0 to 1)
     * @return unit tangent vector
     */
    @Override
    public Vector3 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        double mapped = senseAgreement
                ? trimParamStart + (trimParamEnd - trimParamStart) * parameter
                : trimParamEnd + (trimParamStart - trimParamEnd) * parameter;
        Vector3 tangent = basisCurve.tangentAt(mapped);
        if (!senseAgreement) {
            tangent = tangent.negate();
        }
        return tangent;
    }

    /**
     * Returns the bounding box enclosing the trimmed curve by sampling.
     *
     * @return bounding box enclosing the trimmed segment
     */
    @Override
    public BoundingBox3 boundingBox() {
        BoundingBox3 box = BoundingBox3.empty();
        java.util.List<CartesianPoint> samples = sample(64);
        for (CartesianPoint point : samples) {
            box = box.union(point);
        }
        return box;
    }

    /**
     * Returns the arc length of the trimmed curve segment.
     * Delegates to the basis curve's analytical length when available.
     *
     * @return arc length over the trimmed parameter range
     */
    @Override
    public double length() {
        double tMin = Math.min(trimParamStart, trimParamEnd);
        double tMax = Math.max(trimParamStart, trimParamEnd);
        double fraction = tMax - tMin;
        return switch (basisCurve) {
            case Circle circle -> circle.circumference() * fraction;
            case Ellipse3 ellipse -> ellipse.perimeter() * fraction;
            case Line3 line3 -> {
                yield trimEnd().distanceTo(trimStart());
            }
            case Polyline3 pl -> {
                int n = pl.points().size();
                if (n < 2) yield 0.0;
                double total = 0.0;
                for (int i = 0; i < n - 1; i++) {
                    double segStart = (double) i / (n - 1);
                    double segEnd = (double) (i + 1) / (n - 1);
                    if (segEnd < tMin - Epsilon.EPS || segStart > tMax + Epsilon.EPS) continue;
                    double overlapStart = Math.max(segStart, tMin);
                    double overlapEnd = Math.min(segEnd, tMax);
                    if (overlapEnd > overlapStart) {
                        double segLen = pl.points().get(i).distanceTo(pl.points().get(i + 1));
                        total += segLen * (overlapEnd - overlapStart) / (segEnd - segStart);
                    }
                }
                yield total;
            }
            case BSplineCurve3 bs -> bs.length(tMin, tMax);
            case RationalBSplineCurve3 rb -> rb.length(tMin, tMax);
            case Parabola3 p -> p.length(tMin, tMax);
            case Hyperbola3 h -> h.length(tMin, tMax);
            case TrimmedCurve3 tc -> {
                double outerMin = Math.min(tc.trimParamStart, tc.trimParamEnd);
                double outerMax = Math.max(tc.trimParamStart, tc.trimParamEnd);
                double mappedTMin = outerMin + (outerMax - outerMin) * tMin;
                double mappedTMax = outerMin + (outerMax - outerMin) * tMax;
                double basisFraction = mappedTMax - mappedTMin;
                yield tc.basisCurve().length() * basisFraction;
            }
            case Clothoid3 c -> c.length() * fraction;
            case DegenerateCurve3 deg -> 0.0;
            case CompositeCurve3 cc -> cc.length() * fraction;
            case SurfaceCurve3 sc -> sc.length() * fraction;
            default -> {
                java.util.List<CartesianPoint> samples = sample(256);
                double total = 0.0;
                for (int i = 0; i < samples.size() - 1; i++) {
                    total += samples.get(i).distanceTo(samples.get(i + 1));
                }
                yield total;
            }
        };
    }

    /**
     * Returns the closest point on the trimmed curve to a given point.
     *
     * @param point target point
     * @return closest point on the trimmed curve
     */
    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Sample the trimmed curve at increasing resolution to find initial guess
        CartesianPoint closest = null;
        double minDist = Double.POSITIVE_INFINITY;
        double bestT = 0.0;
        double tMin = Math.min(trimParamStart, trimParamEnd);
        double tMax = Math.max(trimParamStart, trimParamEnd);
        for (int res : new int[]{16, 32, 64}) {
            java.util.List<CartesianPoint> samples = sample(res);
            for (int i = 0; i < samples.size(); i++) {
                double d = point.distanceTo(samples.get(i));
                if (d < minDist) {
                    minDist = d;
                    closest = samples.get(i);
                    double localT = (double) i / (samples.size() - 1);
                    bestT = senseAgreement
                            ? trimParamStart + (trimParamEnd - trimParamStart) * localT
                            : trimParamEnd + (trimParamStart - trimParamEnd) * localT;
                }
            }
        }

        // Newton-Raphson refinement on basis curve: minimize ||C(t) - P||^2
        double t = bestT;
        for (int iter = 0; iter < 20; iter++) {
            CartesianPoint cp = basisCurve.pointAt(t);
            Vector3 residual = cp.subtract(point);
            Vector3 deriv = basisCurve.tangentAt(t);
            // Scale tangent by basis curve speed for proper Newton step
            double derivNormSq = deriv.normSquared();
            if (derivNormSq <= Epsilon.EPS) break;
            double dt = -residual.dot(deriv) / derivNormSq;
            t += dt;
            // Clamp to trimmed range
            t = Math.max(tMin, Math.min(tMax, t));
            if (Math.abs(dt) < 1e-12) break;
        }
        return basisCurve.pointAt(t);
    }

    /**
     * Returns the distance from a point to the trimmed curve.
     *
     * @param point target point
     * @return minimum distance to the trimmed curve
     */
    @Override
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the midpoint of the trimmed curve.
     *
     * @return midpoint
     */
    public CartesianPoint midpoint() {
        return pointAt(0.5);
    }

    /**
     * Returns the curvature of the trimmed curve.
     * Delegates to the basis curve if it has curvature, otherwise returns 0.
     *
     * @return curvature
     */
    public double curvature() {
        if (basisCurve instanceof Circle circle) {
            return circle.curvature();
        } else if (basisCurve instanceof Ellipse3 ellipse) {
            return ellipse.curvatureAt(0);
        } else if (basisCurve instanceof Line3) {
            return 0.0;
        }
        return 0.0;
    }

    /**
     * Returns the curvature at a given parameter.
     *
     * @param parameter parameter value (0 to 1)
     * @return curvature at the parameter
     */
    public double curvatureAt(double parameter) {
        return curvature();
    }

    /**
     * Returns the curve parameter corresponding to the given point.
     * Maps the basis curve parameter back to [0,1] range.
     *
     * @param point a point on or near the trimmed curve
     * @return parameter value in [0,1]
     */
    @Override
    public double parameterAt(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        double basisParam = basisCurve.parameterAt(point);
        double minP = Math.min(trimParamStart, trimParamEnd);
        double maxP = Math.max(trimParamStart, trimParamEnd);
        double range = maxP - minP;
        if (Math.abs(range) <= Epsilon.EPS) {
            return 0.0;
        }
        return (basisParam - minP) / range;
    }
}
