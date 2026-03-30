package com.minicad.step.model;

import java.util.List;

/**
 * Minimal presentation style assignment.
 *
 * @param id STEP instance id
 * @param styles referenced presentation styles
 */
public record StepPresentationStyleAssignment(int id, List<StepEntity> styles) implements StepEntity {

    public StepPresentationStyleAssignment {
        styles = List.copyOf(styles);
    }

    @Override
    public String name() {
        return "";
    }
}
