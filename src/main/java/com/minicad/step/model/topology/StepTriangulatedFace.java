package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TRIANGULATED_FACE.
 * A face represented by a triangulated surface with coordinate references.
 */
public record StepTriangulatedFace(
    int id,
    String name,
    List<StepEntity> vertices,
    List<Integer> indices
) implements StepEntity {

    public StepTriangulatedFace {
        vertices = List.copyOf(vertices);
        indices = List.copyOf(indices);
    }
}
