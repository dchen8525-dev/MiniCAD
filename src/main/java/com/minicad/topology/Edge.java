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

    public Edge(Vertex start, Vertex end, Curve3 curve, boolean sameSense) {
        this.start = start;
        this.end = end;
        this.curve = curve;
        this.sameSense = sameSense;
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
        BoundingBox3 box = curve.boundingBox();
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
        // Sample the curve and sum distances
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
        // Use curve sampling
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
