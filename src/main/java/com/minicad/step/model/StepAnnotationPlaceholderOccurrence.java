package com.minicad.step.model;

import java.util.List;

/**
 * Minimal annotation placeholder occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item referenced point-like carrier
 * @param role placeholder role enum
 * @param lineSpacing positive line spacing
 */
public record StepAnnotationPlaceholderOccurrence(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item,
        String role,
        double lineSpacing
) implements StepEntity {

    public StepAnnotationPlaceholderOccurrence {
        styles = List.copyOf(styles);
    }
}
