package com.minicad.topology;

import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;

/**
 * Marker interface for topological loop subtypes.
 */
public sealed interface Loop permits EdgeLoop, VertexLoop, PolyLoop {

    /**
     * Returns the bounding box enclosing the loop.
     *
     * @return bounding box enclosing the loop
     */
    BoundingBox3 boundingBox();

    /**
     * Returns the approximate centroid of the loop.
     *
     * @return centroid point
     */
    default CartesianPoint centroid() {
        return boundingBox().center();
    }
}
