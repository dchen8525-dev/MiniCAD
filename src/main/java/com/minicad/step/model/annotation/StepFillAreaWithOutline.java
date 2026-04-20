package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FILL_AREA_WITH_OUTLINE.
 */
public record StepFillAreaWithOutline(
    int id,
    String name,
    List<StepEntity> outlines
) implements StepEntity {

    public StepFillAreaWithOutline {
        outlines = List.copyOf(outlines);
    }
}
