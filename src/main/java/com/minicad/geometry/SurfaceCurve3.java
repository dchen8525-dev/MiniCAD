package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Minimal surface-curve wrapper over a supported 3D curve.
 *
 * @param curve3d supported 3D curve
 */
public record SurfaceCurve3(Curve3 curve3d) implements Curve3 {

    /**
     * Creates a surface curve.
     */
    public SurfaceCurve3 {
        Preconditions.requireNonNull(curve3d, "curve3d");
    }

    @Override
    public boolean contains(CartesianPoint point) {
        return curve3d.contains(point);
    }

    /**
     * Samples points along the underlying curve.
     *
     * @param segments number of segments
     * @return sampled points from the underlying curve
     */
    public java.util.List<CartesianPoint> sample(int segments) {
        return sampleCurveInternal(curve3d, segments);
    }

    /**
     * Evaluates a point at the given parameter.
     *
     * @param parameter parameter value
     * @return point on the underlying curve
     */
    @Override
    public CartesianPoint pointAt(double parameter) {
        return getPointOnCurveInternal(curve3d, parameter);
    }

    private static java.util.List<CartesianPoint> sampleCurveInternal(Curve3 curve, int segments) {
        if (curve instanceof BSplineCurve3 bspline) {
            return bspline.sample(segments);
        } else if (curve instanceof RationalBSplineCurve3 rational) {
            return rational.sample(segments);
        } else if (curve instanceof Circle circle) {
            return circle.sample(segments);
        } else if (curve instanceof Ellipse3 ellipse) {
            return ellipse.sample(segments);
        } else if (curve instanceof Line3 line) {
            return line.sample(segments, 0.0, 1.0);
        } else if (curve instanceof Polyline3 polyline) {
            return polyline.points();
        } else if (curve instanceof CompositeCurve3 composite) {
            return sampleCompositeCurveInternal(composite, segments);
        } else if (curve instanceof TrimmedCurve3 trimmed) {
            return trimmed.sample(segments);
        } else if (curve instanceof Hyperbola3 hyperbola) {
            return hyperbola.sample(segments);
        } else if (curve instanceof Parabola3 parabola) {
            return parabola.sample(segments);
        } else if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return sampleCurveInternal(surfaceCurve.curve3d(), segments);
        } else if (curve instanceof Clothoid3 clothoid) {
            return clothoid.sample(segments);
        } else if (curve instanceof DegenerateCurve3 degenerate) {
            return degenerate.sample(segments);
        }
        return java.util.List.of();
    }

    private static CartesianPoint getPointOnCurveInternal(Curve3 curve, double parameter) {
        if (curve instanceof BSplineCurve3 bspline) {
            return bspline.pointAt(parameter);
        } else if (curve instanceof RationalBSplineCurve3 rational) {
            return rational.pointAt(parameter);
        } else if (curve instanceof Circle circle) {
            return circle.pointAt(parameter);
        } else if (curve instanceof Ellipse3 ellipse) {
            return ellipse.pointAt(parameter);
        } else if (curve instanceof Line3 line) {
            return line.pointAt(parameter);
        } else if (curve instanceof Polyline3 polyline) {
            return getPointOnPolyline(polyline, parameter);
        } else if (curve instanceof CompositeCurve3 composite) {
            return getPointOnCompositeInternal(composite, parameter);
        } else if (curve instanceof TrimmedCurve3 trimmed) {
            return trimmed.pointAt(parameter);
        } else if (curve instanceof Hyperbola3 hyperbola) {
            return hyperbola.pointAt(parameter);
        } else if (curve instanceof Parabola3 parabola) {
            return parabola.pointAt(parameter);
        } else if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return getPointOnCurveInternal(surfaceCurve.curve3d(), parameter);
        } else if (curve instanceof Clothoid3 clothoid) {
            return clothoid.pointAt(parameter);
        } else if (curve instanceof DegenerateCurve3 degenerate) {
            return degenerate.pointAt(parameter);
        }
        return new CartesianPoint(0, 0, 0);
    }

    private static CartesianPoint getPointOnPolyline(Polyline3 polyline, double parameter) {
        java.util.List<CartesianPoint> points = polyline.points();
        if (points.isEmpty()) {
            return new CartesianPoint(0, 0, 0);
        }
        int segments = points.size() - 1;
        double t = parameter * segments;
        int index = (int) Math.min(Math.floor(t), segments - 1);
        if (index < 0) index = 0;
        double localT = t - index;
        CartesianPoint p1 = points.get(index);
        CartesianPoint p2 = points.get(index + 1);
        return p1.add(p2.subtract(p1).scale(localT));
    }

    private static CartesianPoint getPointOnCompositeInternal(CompositeCurve3 composite, double parameter) {
        java.util.List<Curve3> segments = composite.segments();
        if (segments.isEmpty()) {
            return new CartesianPoint(0, 0, 0);
        }
        int index = (int) (parameter * segments.size());
        index = Math.max(0, Math.min(index, segments.size() - 1));
        double localT = parameter * segments.size() - index;
        return getPointOnCurveInternal(segments.get(index), localT);
    }

    private static java.util.List<CartesianPoint> sampleCompositeCurveInternal(CompositeCurve3 composite, int segments) {
        java.util.List<CartesianPoint> allPoints = new java.util.ArrayList<>();
        int segmentsPerSegment = Math.max(1, segments / composite.segments().size());
        for (Curve3 segment : composite.segments()) {
            java.util.List<CartesianPoint> segmentPoints = sampleCurveInternal(segment, segmentsPerSegment);
            allPoints.addAll(segmentPoints);
        }
        return java.util.List.copyOf(allPoints);
    }

    /**
     * Computes the tangent vector at a given parameter by delegating to the underlying curve.
     *
     * @param parameter parameter value
     * @return unit tangent vector from the underlying curve
     */
    public Vector3 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        return getTangentOnCurveInternal(curve3d, parameter);
    }

    private static Vector3 getTangentOnCurveInternal(Curve3 curve, double parameter) {
        if (curve instanceof BSplineCurve3 bspline) {
            return bspline.tangentAt(parameter);
        } else if (curve instanceof RationalBSplineCurve3 rational) {
            return rational.tangentAt(parameter);
        } else if (curve instanceof Circle circle) {
            return circle.tangentAt(parameter);
        } else if (curve instanceof Ellipse3 ellipse) {
            return ellipse.tangentAt(parameter);
        } else if (curve instanceof Line3 line) {
            return line.tangentAt(parameter);
        } else if (curve instanceof Polyline3 polyline) {
            return getTangentOnPolyline(polyline, parameter);
        } else if (curve instanceof CompositeCurve3 composite) {
            return getTangentOnCompositeInternal(composite, parameter);
        } else if (curve instanceof TrimmedCurve3 trimmed) {
            return trimmed.tangentAt(parameter);
        } else if (curve instanceof Hyperbola3 hyperbola) {
            return hyperbola.tangentAt(parameter);
        } else if (curve instanceof Parabola3 parabola) {
            return parabola.tangentAt(parameter);
        } else if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return getTangentOnCurveInternal(surfaceCurve.curve3d(), parameter);
        } else if (curve instanceof Clothoid3 clothoid) {
            return clothoid.tangentAt(parameter);
        } else if (curve instanceof DegenerateCurve3 degenerate) {
            return degenerate.tangentAt(parameter);
        }
        // Default tangent using numerical differentiation
        return computeNumericalTangent(curve, parameter);
    }

    private static Vector3 getTangentOnPolyline(Polyline3 polyline, double parameter) {
        java.util.List<CartesianPoint> points = polyline.points();
        int segments = points.size() - 1;
        double t = parameter * segments;
        int index = (int) Math.min(Math.floor(t), segments - 1);
        if (index < 0) index = 0;
        if (index >= segments) index = segments - 1;
        CartesianPoint p1 = points.get(index);
        CartesianPoint p2 = points.get(index + 1);
        Vector3 tangent = p2.subtract(p1);
        if (tangent.norm() <= Epsilon.EPS) {
            return new Vector3(1, 0, 0);
        }
        return tangent.normalize().asVector();
    }

    private static Vector3 getTangentOnCompositeInternal(CompositeCurve3 composite, double parameter) {
        java.util.List<Curve3> segments = composite.segments();
        if (segments.isEmpty()) {
            return new Vector3(1, 0, 0);
        }
        int index = (int) (parameter * segments.size());
        index = Math.max(0, Math.min(index, segments.size() - 1));
        double localT = parameter * segments.size() - index;
        return getTangentOnCurveInternal(segments.get(index), localT);
    }

    private static Vector3 computeNumericalTangent(Curve3 curve, double parameter) {
        double eps = 1e-6;
        CartesianPoint p1 = getPointOnCurveInternal(curve, Math.max(0, parameter - eps));
        CartesianPoint p2 = getPointOnCurveInternal(curve, Math.min(1, parameter + eps));
        Vector3 tangent = p2.subtract(p1);
        if (tangent.norm() <= Epsilon.EPS) {
            return new Vector3(1, 0, 0);
        }
        return tangent.normalize().asVector();
    }

    /**
     * Returns the bounding box by delegating to the underlying curve.
     *
     * @return bounding box enclosing the underlying curve
     */
    public BoundingBox3 boundingBox() {
        return getCurveBoundingBox(curve3d);
    }

    private static BoundingBox3 getCurveBoundingBox(Curve3 curve) {
        if (curve instanceof Circle circle) {
            return circle.boundingBox();
        } else if (curve instanceof Ellipse3 ellipse) {
            return ellipse.boundingBox();
        } else if (curve instanceof Polyline3 polyline) {
            return polyline.boundingBox();
        } else if (curve instanceof TrimmedCurve3 trimmed) {
            return trimmed.boundingBox();
        } else if (curve instanceof BSplineCurve3 bspline) {
            return bspline.boundingBox();
        } else if (curve instanceof RationalBSplineCurve3 rational) {
            return rational.boundingBox();
        } else if (curve instanceof CompositeCurve3 composite) {
            return composite.boundingBox();
        } else if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return surfaceCurve.boundingBox();
        } else if (curve instanceof DegenerateCurve3 degenerate) {
            return degenerate.boundingBox();
        }
        // For other curves, sample and compute
        BoundingBox3 box = BoundingBox3.empty();
        java.util.List<CartesianPoint> samples = sampleCurveInternal(curve, 64);
        for (CartesianPoint point : samples) {
            box = box.union(point);
        }
        return box;
    }

    /**
     * Returns the approximate length of the curve.
     *
     * @return approximate length
     */
    public double length() {
        return getCurveLength(curve3d);
    }

    private static double getCurveLength(Curve3 curve) {
        if (curve instanceof Circle circle) {
            return circle.circumference();
        } else if (curve instanceof Ellipse3 ellipse) {
            return ellipse.perimeter();
        } else if (curve instanceof Polyline3 polyline) {
            return polyline.length();
        } else if (curve instanceof TrimmedCurve3 trimmed) {
            return trimmed.length();
        } else if (curve instanceof BSplineCurve3 bspline) {
            return bspline.length();
        } else if (curve instanceof RationalBSplineCurve3 rational) {
            return rational.length();
        } else if (curve instanceof CompositeCurve3 composite) {
            return composite.length();
        } else if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return surfaceCurve.length();
        }
        // For other curves, approximate by sampling
        java.util.List<CartesianPoint> samples = sampleCurveInternal(curve, 256);
        double length = 0.0;
        for (int i = 0; i < samples.size() - 1; i++) {
            length += samples.get(i).distanceTo(samples.get(i + 1));
        }
        return length;
    }

    /**
     * Returns the closest point on the curve to a given point.
     *
     * @param point target point
     * @return closest point on the curve
     */
    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return getClosestPointOnCurve(curve3d, point);
    }

    private static CartesianPoint getClosestPointOnCurve(Curve3 curve, CartesianPoint point) {
        if (curve instanceof Circle circle) {
            return circle.closestPointTo(point);
        } else if (curve instanceof Ellipse3 ellipse) {
            return ellipse.closestPointTo(point);
        } else if (curve instanceof Polyline3 polyline) {
            return polyline.closestPointTo(point);
        } else if (curve instanceof TrimmedCurve3 trimmed) {
            return trimmed.closestPointTo(point);
        } else if (curve instanceof BSplineCurve3 bspline) {
            return bspline.closestPointTo(point);
        } else if (curve instanceof RationalBSplineCurve3 rational) {
            return rational.closestPointTo(point);
        } else if (curve instanceof CompositeCurve3 composite) {
            return composite.closestPointTo(point);
        } else if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return surfaceCurve.closestPointTo(point);
        } else if (curve instanceof Line3 line) {
            return line.closestPointTo(point);
        } else if (curve instanceof Parabola3 parabola) {
            return parabola.closestPointTo(point);
        } else if (curve instanceof Hyperbola3 hyperbola) {
            return hyperbola.closestPointTo(point);
        } else if (curve instanceof Clothoid3 clothoid) {
            return clothoid.closestPointTo(point);
        } else if (curve instanceof DegenerateCurve3 degenerate) {
            return degenerate.closestPointTo(point);
        }
        // For other curves, find closest by sampling
        java.util.List<CartesianPoint> samples = sampleCurveInternal(curve, 256);
        CartesianPoint closest = samples.get(0);
        double minDistance = point.distanceTo(closest);
        for (int i = 1; i < samples.size(); i++) {
            double distance = point.distanceTo(samples.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                closest = samples.get(i);
            }
        }
        return closest;
    }

    /**
     * Returns the distance from a point to the curve.
     *
     * @param point target point
     * @return minimum distance to the curve
     */
    @Override
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the midpoint of the curve.
     *
     * @return midpoint
     */
    public CartesianPoint midpoint() {
        return pointAt(0.5);
    }

    /**
     * Returns the underlying 3D curve.
     *
     * @return underlying curve
     */
    public Curve3 underlyingCurve() {
        return curve3d;
    }

    /**
     * Returns the curve parameter corresponding to the given point.
     * Delegates to the underlying 3D curve.
     *
     * @param point a point on or near the curve
     * @return parameter value on the underlying curve
     */
    @Override
    public double parameterAt(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return curve3d.parameterAt(point);
    }
}
