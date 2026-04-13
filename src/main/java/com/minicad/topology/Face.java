package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.Plane;
import com.minicad.geometry.SurfaceGeometry;

import java.util.List;

/**
 * Minimal face with optional planar validation.
 *
 * @param surface supporting surface
 * @param bounds face boundaries
 * @param sameSense whether the face orientation matches the surface normal
 */
public record Face(SurfaceGeometry surface, List<FaceBound> bounds, boolean sameSense) {

    /**
     * Creates a face and validates that planar loop vertices lie on the plane.
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
            if (surface instanceof Plane plane && bound.loop() instanceof EdgeLoop edgeLoop) {
                for (OrientedEdge edge : edgeLoop.edges()) {
                    if (!plane.contains(edge.startVertex().point())) {
                        throw new TopologyException("all face vertices must lie on the plane");
                    }
                    if (!plane.contains(edge.endVertex().point())) {
                        throw new TopologyException("all face vertices must lie on the plane");
                    }
                }
            } else if (surface instanceof Plane plane && bound.loop() instanceof VertexLoop vertexLoop) {
                if (!plane.contains(vertexLoop.vertex().point())) {
                    throw new TopologyException("all face vertices must lie on the plane");
                }
            } else if (surface instanceof Plane plane && bound.loop() instanceof PolyLoop polyLoop) {
                for (var point : polyLoop.points()) {
                    if (!plane.contains(point)) {
                        throw new TopologyException("all face vertices must lie on the plane");
                    }
                }
            }
        }
        if (!hasOuter) {
            throw new TopologyException("face must contain an outer bound");
        }
    }
}
