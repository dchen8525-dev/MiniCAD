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
     * Returns the approximate length of the trimmed curve segment.
     *
     * @return length
     */
    @Override
    public double length() {
        java.util.List<CartesianPoint> samples = sample(256);
        double total = 0.0;
        for (int i = 0; i < samples.size() - 1; i++) {
            total += samples.get(i).distanceTo(samples.get(i + 1));
        }
        return total;
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
        // Sample the trimmed curve at increasing resolution
        CartesianPoint closest = null;
        double minDist = Double.POSITIVE_INFINITY;
        for (int res : new int[]{16, 32, 64}) {
            java.util.List<CartesianPoint> samples = sample(res);
            for (CartesianPoint s : samples) {
                double d = point.distanceTo(s);
                if (d < minDist) {
                    minDist = d;
                    closest = s;
                }
            }
        }
        return closest != null ? closest : pointAt(0);
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
