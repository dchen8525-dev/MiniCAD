package com.minicad.step.model;

import java.util.List;

/**
 * Minimal TESSELLATED_FACE.
 * A face defined by a tessellated (triangulated) surface.
 *
 * @param id STEP id
 * @param name STEP label
 * @param triangles list of triangle entities or vertex references
 */
public record StepTessellatedFace(
        int id,
        String name,
        List<StepEntity> triangles
) implements StepEntity {

    public StepTessellatedFace {
        triangles = List.copyOf(triangles);
    }
}
