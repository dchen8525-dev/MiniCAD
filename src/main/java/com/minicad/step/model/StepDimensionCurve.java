package com.minicad.step.model;

import java.util.List;

/**
 * Minimal dimension curve occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item dimension curve geometry
 */
public record StepDimensionCurve(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item
) implements StepEntity {

    public StepDimensionCurve {
        styles = List.copyOf(styles);
    }
}
