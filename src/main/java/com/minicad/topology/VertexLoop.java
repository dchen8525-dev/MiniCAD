package com.minicad.topology;

import com.minicad.common.TopologyException;

/**
 * Degenerate loop represented by a single vertex.
 *
 * @param vertex loop vertex
 */
public record VertexLoop(Vertex vertex) implements Loop {

    /**
     * Creates a vertex loop.
     */
    public VertexLoop {
        if (vertex == null) {
            throw new TopologyException("vertex must not be null");
        }
    }
}
