package com.minicad.step.model;

import java.util.List;

/**
 * Minimal presentation style assignment.
 *
 * @param id STEP instance id
 * @param styles supported style usages
 */
public record StepPresentationStyleAssignment(int id, List<StepSurfaceStyleUsage> styles) implements StepEntity {

    public StepPresentationStyleAssignment {
        styles = List.copyOf(styles);
    }

    @Override
    public String name() {
        return "";
    }
}
