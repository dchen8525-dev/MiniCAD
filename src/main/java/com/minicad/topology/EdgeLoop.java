package com.minicad.topology;

import com.minicad.common.Epsilon;
import com.minicad.common.TopologyException;

import java.util.List;

/**
 * Ordered closed loop of oriented edges.
 *
 * @param edges oriented edges in traversal order
 */
public record EdgeLoop(List<OrientedEdge> edges) {

    /**
     * Creates a loop and validates closure/connectivity.
     */
    public EdgeLoop {
        if (edges == null) {
            throw new TopologyException("edges must not be null");
        }
        if (edges.isEmpty()) {
            throw new TopologyException("edges must not be empty");
        }
        edges = List.copyOf(edges);

        for (int i = 0; i < edges.size(); i++) {
            OrientedEdge current = edges.get(i);
            if (current == null) {
                throw new TopologyException("edges must not contain null");
            }

            OrientedEdge next = edges.get((i + 1) % edges.size());
            double gap = current.endVertex().point().distanceTo(next.startVertex().point());
            if (gap > Epsilon.EPS) {
                throw new TopologyException("edge loop must be connected and closed");
            }
        }
    }
}
