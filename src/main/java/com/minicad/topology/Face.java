package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.Plane;

import java.util.List;

/**
 * Minimal planar face.
 *
 * @param surface supporting plane
 * @param bounds face boundaries
 * @param sameSense whether the face orientation matches the surface normal
 */
public record Face(Plane surface, List<FaceBound> bounds, boolean sameSense) {

    /**
     * Creates a planar face and validates that loop vertices lie on the plane.
     */
    public Face {
        if (surface == null) {
            throw new TopologyException("surface must not be null");
        }
        if (bounds == null) {
            throw new TopologyException("bounds must not be null");
        }
        if (bounds.isEmpty()) {
            throw new TopologyException("bounds must not be empty");
        }
        bounds = List.copyOf(bounds);

        boolean hasOuter = false;
        for (FaceBound bound : bounds) {
            if (bound == null) {
                throw new TopologyException("bounds must not contain null");
            }
            if (bound.outer()) {
                hasOuter = true;
            }
            for (OrientedEdge edge : bound.loop().edges()) {
                if (!surface.contains(edge.startVertex().point())) {
                    throw new TopologyException("all face vertices must lie on the plane");
                }
                if (!surface.contains(edge.endVertex().point())) {
                    throw new TopologyException("all face vertices must lie on the plane");
                }
            }
        }
        if (!hasOuter) {
            throw new TopologyException("face must contain an outer bound");
        }
    }
}
