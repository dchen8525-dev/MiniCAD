package com.minicad.topology;

import com.minicad.common.Epsilon;
import com.minicad.common.TopologyException;
import com.minicad.geometry.Curve3;

/**
 * Minimal topological edge backed by a supported 3D curve.
 *
 * @param start start vertex
 * @param end end vertex
 * @param curve underlying curve geometry
 * @param sameSense whether the topological direction matches the curve direction
 */
public record Edge(Vertex start, Vertex end, Curve3 curve, boolean sameSense) {

    /**
     * Creates an edge and validates its invariants.
     */
    public Edge {
        if (start == null) {
            throw new TopologyException("start must not be null");
        }
        if (end == null) {
            throw new TopologyException("end must not be null");
        }
        if (curve == null) {
            throw new TopologyException("curve must not be null");
        }
        if (start.point().distanceTo(end.point()) <= Epsilon.EPS) {
            throw new TopologyException("edge must have distinct vertices");
        }
        if (!curve.contains(start.point())) {
            throw new TopologyException("start vertex must lie on edge curve");
        }
        if (!curve.contains(end.point())) {
            throw new TopologyException("end vertex must lie on edge curve");
        }
    }
}
