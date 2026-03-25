package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CARTESIAN_POINT.
 *
 * @param id step id
 * @param name step label
 * @param coordinates 3D coordinates
 */
public record StepCartesianPoint(int id, String name, List<Double> coordinates) implements StepEntity {

    /**
     * Creates an immutable point record.
     */
    public StepCartesianPoint {
        coordinates = List.copyOf(coordinates);
    }
}
