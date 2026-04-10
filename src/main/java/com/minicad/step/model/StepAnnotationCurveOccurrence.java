package com.minicad.step.model;

import java.util.List;

/**
 * Minimal annotation curve occurrence for presentation PMI.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item annotated curve item
 */
public record StepAnnotationCurveOccurrence(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item
) implements StepEntity {

    public StepAnnotationCurveOccurrence {
        styles = List.copyOf(styles);
    }
}
