package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.List;

/**
 * Minimal composite curve backed by multiple supported segments.
 *
 * @param segments ordered component curves
 */
public record CompositeCurve3(List<Curve3> segments) implements Curve3 {

    public CompositeCurve3 {
        segments = List.copyOf(segments);
        if (segments.isEmpty()) {
            throw new IllegalArgumentException("segments must not be empty");
        }
        for (Curve3 segment : segments) {
            Preconditions.requireNonNull(segment, "segments");
        }
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        for (Curve3 segment : segments) {
            if (segment.contains(point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Evaluates a point on the composite curve at the given parameter.
     * The parameter is distributed across all segments.
     *
     * @param parameter parameter value (0 to 1)
     * @return point on the composite curve
     */
    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (segments.isEmpty()) {
            return new CartesianPoint(0, 0, 0);
        }
        // Map parameter to segment index
        int index = (int) (parameter * segments.size());
        index = Math.max(0, Math.min(index, segments.size() - 1));
        double localT = parameter * segments.size() - index;
        return getPointOnSegment(segments.get(index), localT);
    }

    private static CartesianPoint getPointOnSegment(Curve3 segment, double parameter) {
        if (segment instanceof BSplineCurve3 bspline) {
            return bspline.pointAt(parameter);
        } else if (segment instanceof RationalBSplineCurve3 rational) {
            return rational.pointAt(parameter);
        } else if (segment instanceof Circle circle) {
            return circle.pointAt(parameter);
        } else if (segment instanceof Ellipse3 ellipse) {
            return ellipse.pointAt(parameter);
        } else if (segment instanceof Line3 line) {
            return line.pointAt(parameter);
        } else if (segment instanceof Polyline3 polyline) {
            return getPointOnPolyline(polyline, parameter);
        } else if (segment instanceof TrimmedCurve3 trimmed) {
            return trimmed.pointAt(parameter);
        } else if (segment instanceof Hyperbola3 hyperbola) {
            return hyperbola.pointAt(parameter);
        } else if (segment instanceof Parabola3 parabola) {
            return parabola.pointAt(parameter);
        } else if (segment instanceof SurfaceCurve3 surfaceCurve) {
            return surfaceCurve.pointAt(parameter);
        } else if (segment instanceof Clothoid3 clothoid) {
            return clothoid.pointAt(parameter);
        } else if (segment instanceof DegenerateCurve3 degenerate) {
            return degenerate.pointAt(parameter);
        } else if (segment instanceof CompositeCurve3 composite) {
            return composite.pointAt(parameter);
        }
        return new CartesianPoint(0, 0, 0);
    }

    private static CartesianPoint getPointOnPolyline(Polyline3 polyline, double parameter) {
        List<CartesianPoint> points = polyline.points();
        if (points.isEmpty()) {
            return new CartesianPoint(0, 0, 0);
        }
        int segs = points.size() - 1;
        double t = parameter * segs;
        int index = (int) Math.min(Math.floor(t), segs - 1);
        if (index < 0) index = 0;
        double localT = t - index;
        CartesianPoint p1 = points.get(index);
        CartesianPoint p2 = points.get(index + 1);
        return p1.add(p2.subtract(p1).scale(localT));
    }

    /**
     * Samples the composite curve by sampling each segment.
     *
     * @param segmentsPerSegment number of segments to sample per curve segment
     * @return list of sampled points
     */
    public java.util.List<CartesianPoint> sample(int segmentsPerSegment) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>();
        for (Curve3 segment : segments) {
            points.addAll(sampleSegment(segment, segmentsPerSegment));
        }
        return java.util.List.copyOf(points);
    }

    private static java.util.List<CartesianPoint> sampleSegment(Curve3 segment, int segs) {
        if (segment instanceof BSplineCurve3 bspline) {
            return bspline.sample(segs);
        } else if (segment instanceof RationalBSplineCurve3 rational) {
            return rational.sample(segs);
        } else if (segment instanceof Circle circle) {
            return circle.sample(segs);
        } else if (segment instanceof Ellipse3 ellipse) {
            return ellipse.sample(segs);
        } else if (segment instanceof Line3 line) {
            return line.sample(segs, 0.0, 1.0);
        } else if (segment instanceof Polyline3 polyline) {
            return polyline.points();
        } else if (segment instanceof TrimmedCurve3 trimmed) {
            return trimmed.sample(segs);
        } else if (segment instanceof Hyperbola3 hyperbola) {
            return hyperbola.sample(segs);
        } else if (segment instanceof Parabola3 parabola) {
            return parabola.sample(segs);
        } else if (segment instanceof SurfaceCurve3 surfaceCurve) {
            return surfaceCurve.sample(segs);
        } else if (segment instanceof Clothoid3 clothoid) {
            return clothoid.sample(segs);
        } else if (segment instanceof DegenerateCurve3 degenerate) {
            return degenerate.sample(segs);
        } else if (segment instanceof CompositeCurve3 composite) {
            return composite.sample(segs);
        }
        return java.util.List.of();
    }

    /**
     * Returns the bounding box enclosing all segments.
     * This is computed by sampling each segment and taking the union.
     *
     * @return bounding box enclosing all segments
     */
    public BoundingBox3 boundingBox() {
        BoundingBox3 box = BoundingBox3.empty();
        for (Curve3 segment : segments) {
            box = box.union(segmentBoundingBox(segment));
        }
        return box;
    }

    private static BoundingBox3 segmentBoundingBox(Curve3 segment) {
        if (segment instanceof Circle circle) {
            return circle.boundingBox();
        } else if (segment instanceof Ellipse3 ellipse) {
            return ellipse.boundingBox();
        } else if (segment instanceof Polyline3 polyline) {
            return polyline.boundingBox();
        } else if (segment instanceof TrimmedCurve3 trimmed) {
            return trimmed.boundingBox();
        } else if (segment instanceof BSplineCurve3 bspline) {
            return bspline.boundingBox();
        } else if (segment instanceof RationalBSplineCurve3 rational) {
            return rational.boundingBox();
        } else if (segment instanceof CompositeCurve3 composite) {
            return composite.boundingBox();
        }
        // For other segments, sample and compute
        BoundingBox3 box = BoundingBox3.empty();
        java.util.List<CartesianPoint> samples = sampleSegment(segment, 64);
        for (CartesianPoint point : samples) {
            box = box.union(point);
        }
        return box;
    }

    /**
     * Returns the total length of the composite curve.
     * Approximated by summing sampled segment lengths.
     *
     * @return approximate total length
     */
    public double length() {
        double totalLength = 0.0;
        for (Curve3 segment : segments) {
            totalLength += segmentLength(segment);
        }
        return totalLength;
    }

    /**
     * Returns the tangent vector at a given parameter.
     *
     * @param parameter parameter value (0 to 1)
     * @return unit tangent vector
     */
    public Vector3 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (segments.isEmpty()) {
            return new Vector3(1, 0, 0);
        }
        int index = (int) (parameter * segments.size());
        index = Math.max(0, Math.min(index, segments.size() - 1));
        double localT = parameter * segments.size() - index;
        return getTangentOnSegment(segments.get(index), localT);
    }

    /**
     * Returns the closest point on the composite curve to a given point.
     *
     * @param point target point
     * @return closest point on the composite curve
     */
    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Curve3 segment : segments) {
            CartesianPoint segmentClosest = closestPointOnSegment(segment, point);
            double distance = point.distanceTo(segmentClosest);
            if (distance < minDistance) {
                minDistance = distance;
                closest = segmentClosest;
            }
        }
        return closest != null ? closest : pointAt(0);
    }

    /**
     * Returns the distance from a point to the composite curve.
     *
     * @param point target point
     * @return minimum distance to the composite curve
     */
    @Override
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the segment count.
     *
     * @return number of segments
     */
    public int segmentCount() {
        return segments.size();
    }

    private static double segmentLength(Curve3 segment) {
        if (segment instanceof Circle circle) {
            return circle.circumference();
        } else if (segment instanceof Ellipse3 ellipse) {
            return ellipse.perimeter();
        } else if (segment instanceof Polyline3 polyline) {
            return polyline.length();
        } else if (segment instanceof TrimmedCurve3 trimmed) {
            return trimmed.length();
        } else if (segment instanceof Line3) {
            return 1.0;  // Default length for infinite line
        } else if (segment instanceof BSplineCurve3 bs) {
            return bs.length();
        } else if (segment instanceof RationalBSplineCurve3 rb) {
            return rb.length();
        } else if (segment instanceof Hyperbola3 h) {
            return h.length();
        } else if (segment instanceof Parabola3 p) {
            return p.length();
        } else if (segment instanceof Clothoid3 c) {
            return c.length();
        } else if (segment instanceof CompositeCurve3 cc) {
            return cc.length();
        } else if (segment instanceof SurfaceCurve3 sc) {
            return sc.length();
        }
        // Fallback: approximate by sampling
        java.util.List<CartesianPoint> samples = sampleSegment(segment, 64);
        double length = 0.0;
        for (int i = 0; i < samples.size() - 1; i++) {
            length += samples.get(i).distanceTo(samples.get(i + 1));
        }
        return length;
    }

    private static Vector3 getTangentOnSegment(Curve3 segment, double parameter) {
        if (segment instanceof BSplineCurve3 bspline) {
            return bspline.tangentAt(bspline.startParameter() + parameter * (bspline.endParameter() - bspline.startParameter()));
        } else if (segment instanceof RationalBSplineCurve3 rational) {
            return rational.tangentAt(rational.startParameter() + parameter * (rational.endParameter() - rational.startParameter()));
        } else if (segment instanceof Circle circle) {
            return circle.tangentAt(parameter * 2 * Math.PI);
        } else if (segment instanceof Ellipse3 ellipse) {
            return ellipse.tangentAt(parameter * 2 * Math.PI);
        } else if (segment instanceof Line3 line) {
            return line.direction().asVector();
        } else if (segment instanceof Polyline3 polyline) {
            return polyline.tangentAt(parameter);
        } else if (segment instanceof TrimmedCurve3 trimmed) {
            return trimmed.tangentAt(parameter);
        } else if (segment instanceof Hyperbola3 hyperbola) {
            return hyperbola.tangentAt(parameter);
        } else if (segment instanceof Parabola3 parabola) {
            return parabola.tangentAt(parameter);
        } else if (segment instanceof SurfaceCurve3 surfaceCurve) {
            return surfaceCurve.tangentAt(parameter);
        } else if (segment instanceof Clothoid3 clothoid) {
            return clothoid.tangentAt(parameter * 2 * Math.PI);
        } else if (segment instanceof DegenerateCurve3 degenerate) {
            return degenerate.tangentAt(parameter);
        }
        return new Vector3(1, 0, 0);
    }

    private static CartesianPoint closestPointOnSegment(Curve3 segment, CartesianPoint point) {
        if (segment instanceof Circle circle) {
            return circle.closestPointTo(point);
        } else if (segment instanceof Ellipse3 ellipse) {
            return ellipse.closestPointTo(point);
        } else if (segment instanceof Polyline3 polyline) {
            return polyline.closestPointTo(point);
        } else if (segment instanceof TrimmedCurve3 trimmed) {
            return trimmed.closestPointTo(point);
        } else if (segment instanceof Line3 line) {
            return line.closestPointTo(point);
        } else if (segment instanceof BSplineCurve3 bspline) {
            return bspline.closestPointTo(point);
        } else if (segment instanceof RationalBSplineCurve3 rational) {
            return rational.closestPointTo(point);
        } else if (segment instanceof SurfaceCurve3 surfaceCurve) {
            return surfaceCurve.closestPointTo(point);
        } else if (segment instanceof Parabola3 parabola) {
            return parabola.closestPointTo(point);
        } else if (segment instanceof Hyperbola3 hyperbola) {
            return hyperbola.closestPointTo(point);
        } else if (segment instanceof Clothoid3 clothoid) {
            return clothoid.closestPointTo(point);
        } else if (segment instanceof DegenerateCurve3 degenerate) {
            return degenerate.closestPointTo(point);
        } else if (segment instanceof CompositeCurve3 composite) {
            return composite.closestPointTo(point);
        }
        // For other curves, find closest by sampling
        java.util.List<CartesianPoint> samples = sampleSegment(segment, 256);
        CartesianPoint closest = samples.get(0);
        double minDist = point.distanceTo(closest);
        for (int i = 1; i < samples.size(); i++) {
            double dist = point.distanceTo(samples.get(i));
            if (dist < minDist) {
                minDist = dist;
                closest = samples.get(i);
            }
        }
        return closest;
    }

    /**
     * Returns the curve parameter corresponding to the given point.
     * The parameter is in [0,1], distributed uniformly across segments.
     *
     * @param point a point on or near the composite curve
     * @return parameter value in [0,1]
     */
    @Override
    public double parameterAt(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        int n = segments.size();
        for (int i = 0; i < n; i++) {
            Curve3 seg = segments.get(i);
            CartesianPoint segClosest = closestPointOnSegment(seg, point);
            double dist = point.distanceTo(segClosest);
            if (dist <= Epsilon.EPS * 10) {
                double localParam = seg.parameterAt(segClosest);
                return (i + Math.max(0, Math.min(1, localParam))) / n;
            }
        }
        // Fallback: find closest segment
        double bestDist = Double.POSITIVE_INFINITY;
        int bestSeg = 0;
        double bestLocalParam = 0;
        for (int i = 0; i < n; i++) {
            Curve3 seg = segments.get(i);
            CartesianPoint segClosest = closestPointOnSegment(seg, point);
            double dist = point.distanceTo(segClosest);
            if (dist < bestDist) {
                bestDist = dist;
                bestSeg = i;
                bestLocalParam = seg.parameterAt(segClosest);
            }
        }
        return (bestSeg + Math.max(0, Math.min(1, bestLocalParam))) / n;
    }
}
