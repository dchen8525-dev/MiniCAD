package com.minicad.step.model;

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
