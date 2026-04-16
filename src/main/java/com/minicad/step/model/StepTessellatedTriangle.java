package com.minicad.step.model;

/**
 * Minimal TESSELLATED_TRIANGLE.
 * A single triangle in a tessellated face.
 *
 * @param id STEP id
 * @param vertices the three vertices of the triangle
 */
public record StepTessellatedTriangle(
        int id,
        String name,
        StepEntity vertex1,
        StepEntity vertex2,
        StepEntity vertex3
) implements StepEntity {
}
