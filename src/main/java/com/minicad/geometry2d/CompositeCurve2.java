package com.minicad.geometry2d;

import com.minicad.common.Preconditions;

import java.util.List;

/**
 * Minimal composite 2D curve backed by multiple supported segments.
 *
 * @param segments ordered component curves
 */
public record CompositeCurve2(List<Curve2> segments) implements Curve2 {

    public CompositeCurve2 {
        segments = List.copyOf(segments);
        if (segments.isEmpty()) {
            throw new IllegalArgumentException("segments must not be empty");
        }
        for (Curve2 segment : segments) {
            Preconditions.requireNonNull(segment, "segments");
        }
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        for (Curve2 segment : segments) {
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
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (segments.isEmpty()) {
            return new Point2(0, 0);
        }
        int index = (int) (parameter * segments.size());
        index = Math.max(0, Math.min(index, segments.size() - 1));
        double localT = parameter * segments.size() - index;
        return getPointOnSegment(segments.get(index), localT);
    }

    private static Point2 getPointOnSegment(Curve2 segment, double parameter) {
        if (segment instanceof BSplineCurve2 bspline) {
            return bspline.pointAt(parameter);
        } else if (segment instanceof RationalBSplineCurve2 rational) {
            return rational.pointAt(parameter);
        } else if (segment instanceof Circle2 circle) {
            return circle.pointAt(parameter);
        } else if (segment instanceof Ellipse2 ellipse) {
            return ellipse.pointAt(parameter);
        } else if (segment instanceof Line2 line) {
            return line.pointAt(parameter);
        } else if (segment instanceof Polyline2 polyline) {
            return polyline.pointAt(parameter);
        } else if (segment instanceof TrimmedCurve2 trimmed) {
            return interpolatePoint(trimmed.trimStart(), trimmed.trimEnd(), parameter);
        } else if (segment instanceof Hyperbola2 hyperbola) {
            return hyperbola.pointAt(parameter);
        } else if (segment instanceof Parabola2 parabola) {
            return parabola.pointAt(parameter);
        } else if (segment instanceof CompositeCurve2 composite) {
            return composite.pointAt(parameter);
        }
        return new Point2(0, 0);
    }

    private static Point2 interpolatePoint(Point2 start, Point2 end, double t) {
        return new Point2(
            start.x() + (end.x() - start.x()) * t,
            start.y() + (end.y() - start.y()) * t
        );
    }

    /**
     * Samples the composite curve by sampling each segment.
     *
     * @param segmentsPerSegment number of segments to sample per curve segment
     * @return list of sampled points
     */
    public java.util.List<Point2> sample(int segmentsPerSegment) {
        java.util.List<Point2> points = new java.util.ArrayList<>();
        for (Curve2 segment : segments) {
            points.addAll(sampleSegment(segment, segmentsPerSegment));
        }
        return java.util.List.copyOf(points);
    }

    private static java.util.List<Point2> sampleSegment(Curve2 segment, int segs) {
        if (segment instanceof BSplineCurve2 bspline) {
            return bspline.sample(segs);
        } else if (segment instanceof RationalBSplineCurve2 rational) {
            return rational.sample(segs);
        } else if (segment instanceof Circle2 circle) {
            return circle.sample(segs);
        } else if (segment instanceof Ellipse2 ellipse) {
            return ellipse.sample(segs);
        } else if (segment instanceof Hyperbola2 hyperbola) {
            return hyperbola.sample(segs);
        } else if (segment instanceof Parabola2 parabola) {
            return parabola.sample(segs);
        } else if (segment instanceof Line2 line) {
            return line.sample(segs, 0.0, 1.0);
        } else if (segment instanceof TrimmedCurve2 trimmed) {
            return trimmed.sample(segs);
        } else if (segment instanceof Polyline2 polyline) {
            return polyline.points();
        } else if (segment instanceof CompositeCurve2 composite) {
            return composite.sample(segs);
        }
        return java.util.List.of();
    }

    /**
     * Returns the total length of the composite curve.
     *
     * @return approximate total length
     */
    public double length() {
        double totalLength = 0.0;
        for (Curve2 segment : segments) {
            totalLength += segmentLength(segment);
        }
        return totalLength;
    }

    private static double segmentLength(Curve2 segment) {
        if (segment instanceof Circle2 circle) {
            return circle.circumference();
        } else if (segment instanceof Ellipse2 ellipse) {
            return ellipse.perimeter();
        } else if (segment instanceof Polyline2 polyline) {
            return polyline.length();
        } else if (segment instanceof TrimmedCurve2 trimmed) {
            return trimmed.length();
        } else if (segment instanceof Line2) {
            return 1.0;
        } else if (segment instanceof BSplineCurve2 bs) {
            return bs.length();
        } else if (segment instanceof RationalBSplineCurve2 rb) {
            return rb.length();
        } else if (segment instanceof Parabola2 p) {
            return p.length();
        } else if (segment instanceof Hyperbola2 h) {
            return h.length();
        } else if (segment instanceof DegenerateCurve2) {
            return 0.0;
        } else if (segment instanceof CompositeCurve2 cc) {
            return cc.length();
        }
        // Fallback: approximate by sampling
        java.util.List<Point2> samples = sampleSegment(segment, 64);
        double length = 0.0;
        for (int i = 0; i < samples.size() - 1; i++) {
            length += samples.get(i).distanceTo(samples.get(i + 1));
        }
        return length;
    }

    /**
     * Returns the bounding box enclosing all segments.
     *
     * @return bounding box enclosing all segments
     */
    public BoundingBox2 boundingBox() {
        BoundingBox2 box = BoundingBox2.empty();
        for (Curve2 segment : segments) {
            box = box.union(segmentBoundingBox(segment));
        }
        return box;
    }

    private static BoundingBox2 segmentBoundingBox(Curve2 segment) {
        if (segment instanceof Circle2 circle) {
            return circle.boundingBox();
        } else if (segment instanceof Ellipse2 ellipse) {
            return ellipse.boundingBox();
        } else if (segment instanceof Polyline2 polyline) {
            return polyline.boundingBox();
        } else if (segment instanceof TrimmedCurve2 trimmed) {
            return trimmed.boundingBox();
        }
        // For other segments, sample and compute
        java.util.List<Point2> samples = sampleSegment(segment, 64);
        return BoundingBox2.of(samples);
    }

    /**
     * Returns the tangent vector at a given parameter.
     *
     * @param parameter parameter value (0 to 1)
     * @return unit tangent vector
     */
    public Vector2 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (segments.isEmpty()) {
            return new Vector2(1, 0);
        }
        int index = (int) (parameter * segments.size());
        index = Math.max(0, Math.min(index, segments.size() - 1));
        double localT = parameter * segments.size() - index;
        return getTangentOnSegment(segments.get(index), localT);
    }

    private static Vector2 getTangentOnSegment(Curve2 segment, double parameter) {
        if (segment instanceof Line2 line) {
            return line.tangentAt(parameter);
        } else if (segment instanceof Circle2 circle) {
            return circle.tangentAt(parameter * 2 * Math.PI);
        } else if (segment instanceof Ellipse2 ellipse) {
            return ellipse.tangentAt(parameter * 2 * Math.PI);
        } else if (segment instanceof Polyline2 polyline) {
            return polyline.tangentAt(parameter);
        } else if (segment instanceof TrimmedCurve2 trimmed) {
            return trimmed.tangentAt(parameter);
        } else if (segment instanceof BSplineCurve2 bs) {
            return bs.tangentAt(bs.startParameter() + parameter * (bs.endParameter() - bs.startParameter()));
        } else if (segment instanceof RationalBSplineCurve2 rb) {
            return rb.tangentAt(rb.startParameter() + parameter * (rb.endParameter() - rb.startParameter()));
        } else if (segment instanceof Hyperbola2 hyperbola) {
            return hyperbola.tangentAt(parameter);
        } else if (segment instanceof Parabola2 parabola) {
            return parabola.tangentAt(parameter);
        } else if (segment instanceof DegenerateCurve2) {
            return new Vector2(1, 0);
        } else if (segment instanceof CompositeCurve2 composite) {
            return composite.tangentAt(parameter);
        }
        // Default: numerical differentiation
        double eps = 1e-6;
        Point2 p1 = getPointOnSegment(segment, Math.max(0, parameter - eps));
        Point2 p2 = getPointOnSegment(segment, Math.min(1, parameter + eps));
        Vector2 tangent = p2.subtract(p1);
        if (tangent.isZero()) {
            return new Vector2(1, 0);
        }
        return tangent.normalize().asVector();
    }

    /**
     * Returns the closest point on the composite curve to a given point.
     *
     * @param point target point
     * @return closest point on the composite curve
     */
    public Point2 closestPointTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Point2 closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Curve2 segment : segments) {
            Point2 segmentClosest = closestPointOnSegment(segment, point);
            double distance = point.distanceTo(segmentClosest);
            if (distance < minDistance) {
                minDistance = distance;
                closest = segmentClosest;
            }
        }
        return closest != null ? closest : pointAt(0);
    }

    private static Point2 closestPointOnSegment(Curve2 segment, Point2 point) {
        if (segment instanceof Circle2 circle) {
            return circle.closestPointTo(point);
        } else if (segment instanceof Ellipse2 ellipse) {
            return ellipse.closestPointTo(point);
        } else if (segment instanceof Polyline2 polyline) {
            return polyline.closestPointTo(point);
        } else if (segment instanceof TrimmedCurve2 trimmed) {
            return trimmed.closestPointTo(point);
        } else if (segment instanceof Line2 line) {
            return line.closestPoint(point);
        } else if (segment instanceof BSplineCurve2 bs) {
            return bs.closestPointTo(point);
        } else if (segment instanceof RationalBSplineCurve2 rb) {
            return rb.closestPointTo(point);
        } else if (segment instanceof Hyperbola2 hyperbola) {
            return hyperbola.closestPointTo(point);
        } else if (segment instanceof Parabola2 parabola) {
            return parabola.closestPointTo(point);
        } else if (segment instanceof DegenerateCurve2 deg) {
            return deg.point();
        } else if (segment instanceof CompositeCurve2 composite) {
            return composite.closestPointTo(point);
        }
        // Fallback: find closest by sampling
        java.util.List<Point2> samples = sampleSegment(segment, 256);
        Point2 closest = samples.get(0);
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
     * Returns the distance from a point to the composite curve.
     *
     * @param point target point
     * @return minimum distance to the composite curve
     */
    public double distanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the parameter value (0 to 1) for the closest point on the composite curve.
     * The parameter is distributed across all segments (each segment gets equal parameter range).
     *
     * @param point point to project
     * @return parameter value in [0, 1]
     */
    public double parameterOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Point2 closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        double bestT = 0.0;
        int n = segments.size();
        for (Curve2 segment : segments) {
            Point2 segmentClosest = segment.closestPointTo(point);
            double distance = point.distanceTo(segmentClosest);
            if (distance < minDistance) {
                minDistance = distance;
                closest = segmentClosest;
                double segmentT = parameterOfOnSegment(segment, closest);
                bestT = ((double) segments.indexOf(segment) + segmentT) / n;
            }
        }
        return bestT;
    }

    private static double parameterOfOnSegment(Curve2 segment, Point2 point) {
        // Analytical cases for types with known parameter mappings
        if (segment instanceof Line2 line) {
            // Line2 parameterOf returns raw parameter; for composite [0,1] range, clamp
            double raw = line.parameterOf(point);
            return Math.max(0.0, Math.min(1.0, raw));
        } else if (segment instanceof Circle2 circle) {
            double angle = circle.angleOf(point);
            return angle / (2.0 * Math.PI);
        } else if (segment instanceof Ellipse2 ellipse) {
            double angle = ellipse.angleOf(point);
            return angle / (2.0 * Math.PI);
        } else if (segment instanceof Polyline2 polyline) {
            return polyline.parameterOf(point);
        } else if (segment instanceof CompositeCurve2 composite) {
            return composite.parameterOf(point);
        } else if (segment instanceof DegenerateCurve2) {
            return 0.0;
        }
        // Fallback: sampling-based parameter lookup
        java.util.List<Point2> samples = sampleSegment(segment, 64);
        double minD = Double.POSITIVE_INFINITY;
        double bestT = 0.0;
        for (int i = 0; i < samples.size(); i++) {
            double d = point.distanceTo(samples.get(i));
            if (d < minD) {
                minD = d;
                bestT = (double) i / (samples.size() - 1);
            }
        }
        return bestT;
    }

    /**
     * Returns the segment count.
     *
     * @return number of segments
     */
    public int segmentCount() {
        return segments.size();
    }
}
