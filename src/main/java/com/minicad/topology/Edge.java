package com.minicad.topology;

import com.minicad.common.Epsilon;
import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.TrimmedCurve3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Minimal topological edge backed by a supported 3D curve.
 *
 * @param start start vertex
 * @param end end vertex
 * @param curve underlying curve geometry
 * @param sameSense whether the topological direction matches the curve direction
 */
/**
 * Minimal topological edge backed by a supported 3D curve.
 *
 * @param start start vertex
 * @param end end vertex
 * @param curve underlying curve geometry
 * @param sameSense whether the topological direction matches the curve direction
 */
public final class Edge {
    private final Vertex start;
    private final Vertex end;
    private final Curve3 curve;
    private final boolean sameSense;
    // parameterAt is a 1024-sample scan on B-spline curves; sample()/length()/
    // boundingBox() recompute it per call, so memoize the pair (Edge is immutable).
    private volatile double[] endpointParameters;

    public Edge(Vertex start, Vertex end, Curve3 curve, boolean sameSense) {
        if (start == null || end == null || curve == null) {
            throw new TopologyException("edge vertices and curve are required");
        }
        // Check if this is a degenerate edge (start == end within tolerance)
        double vertexDistance = start.point().distanceTo(end.point());
        boolean isDegenerateEdge = vertexDistance <= Epsilon.IMPORT_TOPOLOGY_TOLERANCE;

        if (isDegenerateEdge && !isClosedCurve(curve)) {
            // Degenerate edges are only allowed on closed curves (circle, ellipse, closed B-spline)
            throw new TopologyException("edge must have distinct vertices unless on a closed curve");
        }
        this.start = start;
        this.end = end;
        this.curve = curve;
        this.sameSense = sameSense;
    }

    /**
     * One closed-curve family: matched by concrete type, decided by its
     * handler. All seven curve types involved are {@code final} classes
     * directly implementing {@link Curve3}, so they are mutually exclusive --
     * first-match-wins dispatch over the table is equivalent to the original
     * if-chain, including the branches that used to fall through on an empty
     * or too-short geometry (no later rule matched them either).
     */
    private record ClosedCurveRule(Class<? extends Curve3> type, Predicate<Curve3> handler) {
    }

    private static final List<ClosedCurveRule> CLOSED_CURVE_RULES = List.of(
            new ClosedCurveRule(Circle.class, curve -> true),
            new ClosedCurveRule(Ellipse3.class, curve -> true),
            new ClosedCurveRule(BSplineCurve3.class, curve -> isClosedBSpline((BSplineCurve3) curve)),
            new ClosedCurveRule(RationalBSplineCurve3.class,
                    curve -> isClosedRationalBSpline((RationalBSplineCurve3) curve)),
            new ClosedCurveRule(CompositeCurve3.class, curve -> isClosedComposite((CompositeCurve3) curve)),
            new ClosedCurveRule(TrimmedCurve3.class, curve -> isClosedCurve(((TrimmedCurve3) curve).getBasisCurve())));

    /**
     * Checks if a curve is closed (start and end points coincide by construction).
     * Closed curves include Circle, Ellipse, and closed B-Splines.
     */
    public static boolean isClosedCurve(Curve3 curve) {
        for (ClosedCurveRule rule : CLOSED_CURVE_RULES) {
            if (rule.type().isInstance(curve)) {
                return rule.handler().test(curve);
            }
        }
        return false;  // Line3, Polyline3, SurfaceCurve3 are not closed
    }

    private static boolean isClosedBSpline(BSplineCurve3 bspline) {
        // A B-Spline is closed if first and last control points coincide
        if (bspline.getControlPoints().size() >= 2) {
            CartesianPoint first = bspline.getControlPoints().get(0);
            CartesianPoint last = bspline.getControlPoints().get(bspline.getControlPoints().size() - 1);
            return first.distanceTo(last) <= Epsilon.EPS;
        }
        return false;
    }

    private static boolean isClosedRationalBSpline(RationalBSplineCurve3 rational) {
        // A rational B-Spline is closed if first and last control points coincide
        if (rational.getControlPoints().size() >= 2) {
            CartesianPoint first = rational.getControlPoints().get(0);
            CartesianPoint last = rational.getControlPoints().get(rational.getControlPoints().size() - 1);
            return first.distanceTo(last) <= Epsilon.EPS;
        }
        return false;
    }

    private static boolean isClosedComposite(CompositeCurve3 composite) {
        // Composite curve is closed if its segments form a closed loop
        return composite.getSegments().size() > 0 && isClosedCompositeCurve(composite);
    }

    /**
     * Checks if a composite curve forms a closed loop.
     */
    private static boolean isClosedCompositeCurve(CompositeCurve3 composite) {
        java.util.List<Curve3> segments = composite.getSegments();
        if (segments.isEmpty()) {
            return false;
        }
        // Sample start of first segment and end of last segment
        try {
            CartesianPoint firstStart = segments.get(0).sample(2).get(0);
            java.util.List<CartesianPoint> lastSamples = segments.get(segments.size() - 1).sample(2);
            CartesianPoint lastEnd = lastSamples.get(lastSamples.size() - 1);
            return firstStart.distanceTo(lastEnd) <= Epsilon.EPS;
        } catch (Exception e) {
            return false;  // Conservative: assume not closed if we can't determine
        }
    }

    public Vertex getStart() {
        return start;
    }

    public Vertex getEnd() {
        return end;
    }

    public Curve3 getCurve() {
        return curve;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    // Record-style accessors
    public Vertex start() { return getStart(); }
    public Vertex end() { return getEnd(); }
    public Curve3 curve() { return getCurve(); }
    public boolean sameSense() { return isSameSense(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge that = (Edge) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(curve, that.curve) && sameSense == that.sameSense;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, curve, sameSense);
    }

    @Override
    public String toString() {
        return "Edge{" + "start=" + start + "end=" + end + "curve=" + curve + "sameSense=" + sameSense + "}";
    }

    /**
     * Returns the bounding box of this edge.
     *
     * @return bounding box enclosing the edge
     */
    public BoundingBox3 boundingBox() {
        if (curve == null) {
            return BoundingBox3.empty();
        }
        BoundingBox3 box = BoundingBox3.of(sample(64));
        // Ensure start and end vertices are included
        if (start != null && start.point() != null) {
            box = box.expand(start.point());
        }
        if (end != null && end.point() != null) {
            box = box.expand(end.point());
        }
        return box;
    }

    /**
     * Returns the length of this edge using curve sampling.
     *
     * @return approximate length of the edge
     */
    public double length() {
        if (curve == null) {
            return 0.0;
        }
        if (start != null && end != null && start.point().equals(end.point())) {
            if (curve instanceof Circle) {
                return ((Circle) curve).circumference();
            }
            return curve.length();
        }
        // Sample the bounded curve segment and sum distances
        java.util.List<CartesianPoint> samples = sample(20);
        double length = 0.0;
        for (int i = 1; i < samples.size(); i++) {
            length += samples.get(i - 1).distanceTo(samples.get(i));
        }
        return length;
    }

    /**
     * Returns the closest point on this edge to a given point.
     *
     * @param point the target point
     * @return closest point on the edge
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        if (curve == null || point == null) {
            return start != null ? start.point() : null;
        }
        // Sample the curve and find closest
        java.util.List<CartesianPoint> samples = sample(50);
        CartesianPoint closest = null;
        double minDist = Double.MAX_VALUE;
        for (CartesianPoint sample : samples) {
            double dist = sample.distanceTo(point);
            if (dist < minDist) {
                minDist = dist;
                closest = sample;
            }
        }
        return closest;
    }

    /**
     * Returns the distance from this edge to a given point.
     *
     * @param point the target point
     * @return distance to the closest point on the edge
     */
    public double distanceTo(CartesianPoint point) {
        if (point == null) {
            return Double.MAX_VALUE;
        }
        CartesianPoint closest = closestPointTo(point);
        return closest != null ? point.distanceTo(closest) : Double.MAX_VALUE;
    }

    /**
     * Returns the midpoint of this edge using sampling.
     *
     * @return approximate midpoint
     */
    public CartesianPoint midpoint() {
        if (curve == null) {
            if (start != null && end != null) {
                return start.point().add(end.point().subtract(start.point()).scale(0.5));
            }
            return start != null ? start.point() : null;
        }
        java.util.List<CartesianPoint> samples = sample(2);
        if (samples.size() >= 2) {
            return samples.get(0).add(samples.get(samples.size() - 1).subtract(samples.get(0)).scale(0.5));
        }
        return samples.isEmpty() ? null : samples.get(0);
    }

    /**
     * Returns sampled points along this edge.
     *
     * @param count number of sample points
     * @return list of sampled points
     */
    public java.util.List<CartesianPoint> sample(int count) {
        if (curve == null) {
            java.util.List<CartesianPoint> result = new java.util.ArrayList<>();
            if (start != null && start.point() != null) {
                result.add(start.point());
            }
            if (end != null && end.point() != null && (start == null || !start.point().equals(end.point()))) {
                result.add(end.point());
            }
            return result;
        }
        if (start != null && end != null && !start.point().equals(end.point())) {
            double[] parameters = endpointParameters;
            if (parameters == null) {
                parameters = new double[] {curve.parameterAt(start.point()), curve.parameterAt(end.point())};
                endpointParameters = parameters;
            }
            double startParameter = parameters[0];
            double endParameter = parameters[1];
            java.util.List<CartesianPoint> result = new java.util.ArrayList<>();
            for (int i = 0; i <= count; i++) {
                double fraction = (double) i / count;
                double parameter = startParameter + (endParameter - startParameter) * fraction;
                result.add(curve.pointAt(parameter));
            }
            if (!sameSense) {
                java.util.Collections.reverse(result);
            }
            return java.util.List.copyOf(result);
        }
        java.util.List<CartesianPoint> curveSamples = curve.sample(count);
        if (curveSamples.isEmpty()) {
            java.util.List<CartesianPoint> result = new java.util.ArrayList<>();
            if (start != null) result.add(start.point());
            if (end != null) result.add(end.point());
            return result;
        }
        return curveSamples;
    }

    /**
     * Checks if this edge contains a point within tolerance.
     *
     * @param point the point to check
     * @return true if the point lies on the edge within tolerance
     */
    public boolean contains(CartesianPoint point) {
        if (point == null) {
            return false;
        }
        CartesianPoint closest = closestPointTo(point);
        return closest != null && point.distanceTo(closest) < Epsilon.get();
    }
}
