package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal annotation fill area occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item referenced fill area
 * @param fillStyleTarget target point for fill styling
 */
public record StepAnnotationFillAreaOccurrence(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepAnnotationFillArea item,
        StepEntity fillStyleTarget
) implements StepEntity {

    public StepAnnotationFillAreaOccurrence {
        styles = List.copyOf(styles);
    }
}
