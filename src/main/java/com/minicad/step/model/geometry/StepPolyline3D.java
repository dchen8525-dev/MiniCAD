package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved POLYLINE_3D.
 */
public record StepPolyline3D(
    int id,
    String name,
    List<StepEntity> points
) implements StepEntity {

    public StepPolyline3D {
        points = List.copyOf(points);
    }
}
