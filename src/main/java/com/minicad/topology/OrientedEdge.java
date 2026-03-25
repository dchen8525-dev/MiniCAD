package com.minicad.topology;

import com.minicad.common.TopologyException;

/**
 * Use of an edge with an explicit orientation inside a loop.
 *
 * @param edge referenced base edge
 * @param orientation true for forward, false for reversed
 */
public record OrientedEdge(Edge edge, boolean orientation) {

    /**
     * Creates an oriented edge.
     */
    public OrientedEdge {
        if (edge == null) {
            throw new TopologyException("edge must not be null");
        }
    }

    /**
     * Returns the effective start vertex under the orientation.
     *
     * @return oriented start vertex
     */
    public Vertex startVertex() {
        return orientation ? edge.start() : edge.end();
    }

    /**
     * Returns the effective end vertex under the orientation.
     *
     * @return oriented end vertex
     */
    public Vertex endVertex() {
        return orientation ? edge.end() : edge.start();
    }
}
