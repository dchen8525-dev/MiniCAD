package com.minicad.step.model;

import java.util.List;

/**
 * Minimal ANNOTATION_SYMBOL_OCCURRENCE.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles presentation style assignments
 * @param item referenced supported annotation content or occurrence
 */
public record StepAnnotationSymbolOccurrence(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item
) implements StepEntity {

    public StepAnnotationSymbolOccurrence {
        styles = List.copyOf(styles);
    }
}
