package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CARTESIAN_POINT.
 *
 * @param id step id
 * @param name step label
 * @param coordinates 2D or 3D coordinates
 */
public record StepCartesianPoint(int id, String name, List<Double> coordinates) implements StepEntity {

    /**
     * Creates an immutable point record.
     */
    public StepCartesianPoint {
        coordinates = List.copyOf(coordinates);
    }
}
