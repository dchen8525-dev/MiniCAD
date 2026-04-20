package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal annotation fill area geometry.
 *
 * @param id STEP instance id
 * @param name representation item name
 * @param boundaries fill boundaries
 */
public record StepAnnotationFillArea(
        int id,
        String name,
        List<StepEntity> boundaries
) implements StepEntity {

    public StepAnnotationFillArea {
        boundaries = List.copyOf(boundaries);
    }
}
