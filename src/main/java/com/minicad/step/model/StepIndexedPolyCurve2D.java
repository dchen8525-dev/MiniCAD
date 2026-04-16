package com.minicad.step.model;

import java.util.List;

/**
 * Resolved INDEXED_POLY_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param points control points
 * @param indices indices into the point list defining the poly curve
 */
public record StepIndexedPolyCurve2D(
        int id,
        String name,
        List<StepCartesianPoint> points,
        List<Integer> indices
) implements StepEntity {
    public StepIndexedPolyCurve2D {
        points = List.copyOf(points);
        indices = List.copyOf(indices);
    }
}
