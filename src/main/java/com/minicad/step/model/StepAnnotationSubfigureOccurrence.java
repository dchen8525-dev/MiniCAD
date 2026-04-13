package com.minicad.step.model;

import java.util.List;

/**
 * Minimal annotation subfigure occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles assigned styles
 * @param item referenced supported annotation content or occurrence
 */
public record StepAnnotationSubfigureOccurrence(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item
) implements StepEntity {

    public StepAnnotationSubfigureOccurrence {
        styles = List.copyOf(styles);
    }
}
