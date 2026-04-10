package com.minicad.step.model;

import java.util.List;

/**
 * Minimal annotation plane occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item referenced plane
 * @param elements optional annotation plane elements
 */
public record StepAnnotationPlane(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepPlane item,
        List<StepEntity> elements
) implements StepEntity {

    public StepAnnotationPlane {
        styles = List.copyOf(styles);
        elements = List.copyOf(elements);
    }
}
