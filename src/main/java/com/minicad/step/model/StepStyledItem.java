package com.minicad.step.model;

import java.util.List;

/**
 * Minimal styled item binding a style assignment to an item.
 *
 * @param id STEP instance id
 * @param name style label
 * @param styles style assignments
 * @param item styled target item
 */
public record StepStyledItem(int id, String name, List<StepPresentationStyleAssignment> styles, StepEntity item) implements StepEntity {

    public StepStyledItem {
        styles = List.copyOf(styles);
    }
}
