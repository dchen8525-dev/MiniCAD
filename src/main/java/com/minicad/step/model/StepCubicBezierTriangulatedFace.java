package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CUBIC_BEZIER_TRIANGULATED_FACE.
 * A triangulated face where edges are represented by cubic Bezier curves.
 */
public record StepCubicBezierTriangulatedFace(
    int id,
    String name,
    List<StepEntity> controlPoints,
    List<Integer> indices
) implements StepEntity {

    public StepCubicBezierTriangulatedFace {
        controlPoints = List.copyOf(controlPoints);
        indices = List.copyOf(indices);
    }
}
