package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
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
