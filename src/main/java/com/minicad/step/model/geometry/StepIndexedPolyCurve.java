package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved INDEXED_POLYCURVE.
 */
public record StepIndexedPolycurve(
    int id,
    String name,
    List<StepEntity> points,
    List<Integer> indices,
    List<StepEntity> segments
) implements StepEntity {

    public StepIndexedPolycurve {
        points = List.copyOf(points);
        indices = List.copyOf(indices);
        segments = List.copyOf(segments);
    }
}
