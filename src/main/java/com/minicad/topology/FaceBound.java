package com.minicad.topology;

import com.minicad.common.TopologyException;

/**
 * Face boundary based on a loop subtype.
 *
 * @param loop boundary loop
 * @param orientation orientation relative to the face
 * @param outer whether this is the outer boundary
 */
public record FaceBound(Loop loop, boolean orientation, boolean outer) {

    /**
     * Creates a face bound.
     */
    public FaceBound {
        if (loop == null) {
            throw new TopologyException("loop must not be null");
        }
    }

    /**
     * Creates an outer bound.
     *
     * @param loop edge loop
     * @param orientation orientation relative to the face
     * @return outer face bound
     */
    public static FaceBound outer(Loop loop, boolean orientation) {
        return new FaceBound(loop, orientation, true);
    }

    /**
     * Creates an inner bound.
     *
     * @param loop edge loop
     * @param orientation orientation relative to the face
     * @return inner face bound
     */
    public static FaceBound inner(Loop loop, boolean orientation) {
        return new FaceBound(loop, orientation, false);
    }
}
