package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved DIRECTION.
 *
 * @param id step id
 * @param name step label
 * @param directionRatios 3D direction ratios
 */
public record StepDirection(int id, String name, List<Double> directionRatios) implements StepEntity {

    /**
     * Creates an immutable direction record.
     */
    public StepDirection {
        directionRatios = List.copyOf(directionRatios);
    }
}
