package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved INDEXED_POLY_CURVE / INDEXED_POLYCURVE (MiniCAD alias).
 */
public record StepIndexedPolyCurve(
    int id,
    String name,
    List<StepCartesianPoint> points,
    List<Integer> indices,
    boolean closed
) implements StepEntity {

    public StepIndexedPolyCurve {
        points = List.copyOf(points);
        indices = List.copyOf(indices);
    }
}
