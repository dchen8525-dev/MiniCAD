package com.minicad.step.model;

import java.util.List;

/**
 * Resolved POLYLINE_2D.
 *
 * @param id step id
 * @param name step label
 * @param points ordered list of points defining the polyline
 */
public record StepPolyline2D(
        int id,
        String name,
        List<StepCartesianPoint> points
) implements StepEntity {
    public StepPolyline2D {
        points = List.copyOf(points);
    }
}
