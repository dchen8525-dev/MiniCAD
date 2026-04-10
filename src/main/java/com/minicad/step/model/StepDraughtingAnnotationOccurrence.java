package com.minicad.step.model;

import java.util.List;

/**
 * Minimal draughting annotation occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles assigned styles
 * @param item styled target item
 */
public record StepDraughtingAnnotationOccurrence(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item
) implements StepEntity {

    public StepDraughtingAnnotationOccurrence {
        styles = List.copyOf(styles);
    }
}
