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
}
