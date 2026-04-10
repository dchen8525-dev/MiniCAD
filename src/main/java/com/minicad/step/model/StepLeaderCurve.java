package com.minicad.step.model;

import java.util.List;

/**
 * Minimal leader curve presentation occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item leader curve geometry
 */
public record StepLeaderCurve(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item
) implements StepEntity {

    public StepLeaderCurve {
        styles = List.copyOf(styles);
    }
}
