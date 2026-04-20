package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal annotation point occurrence for presentation PMI.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item annotated point item
 */
public record StepAnnotationPointOccurrence(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item
) implements StepEntity {

    public StepAnnotationPointOccurrence {
        styles = List.copyOf(styles);
    }
}
