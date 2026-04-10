package com.minicad.step.model;

import java.util.List;

/**
 * Minimal projection curve occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item projected curve geometry
 */
public record StepProjectionCurve(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item
) implements StepEntity {

    public StepProjectionCurve {
        styles = List.copyOf(styles);
    }
}
