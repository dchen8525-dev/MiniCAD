package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COMPLEX_TRIANGULATED_FACE.
 * A triangulated face with multiple outer and inner boundaries.
 */
public record StepComplexTriangulatedFace(
    int id,
    String name,
    List<StepEntity> boundaries,
    List<StepEntity> vertices
) implements StepEntity {

    public StepComplexTriangulatedFace {
        boundaries = List.copyOf(boundaries);
        vertices = List.copyOf(vertices);
    }
}
