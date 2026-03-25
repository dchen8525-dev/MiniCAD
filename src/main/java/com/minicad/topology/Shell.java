package com.minicad.topology;

import com.minicad.common.TopologyException;

import java.util.List;

/**
 * Minimal shell made of planar faces.
 *
 * @param faces faces of the shell
 * @param closed whether the shell is declared closed
 */
public record Shell(List<Face> faces, boolean closed) {

    /**
     * Creates a shell.
     */
    public Shell {
        if (faces == null) {
            throw new TopologyException("faces must not be null");
        }
        if (faces.isEmpty()) {
            throw new TopologyException("faces must not be empty");
        }
        faces = List.copyOf(faces);
        for (Face face : faces) {
            if (face == null) {
                throw new TopologyException("faces must not contain null");
            }
        }
    }
}
