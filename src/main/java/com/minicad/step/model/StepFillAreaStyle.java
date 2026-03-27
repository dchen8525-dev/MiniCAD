package com.minicad.step.model;

import java.util.List;

/**
 * Minimal fill area style containing fill colour definitions.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styles supported fill area style components
 */
public record StepFillAreaStyle(int id, String name, List<StepFillAreaStyleColour> styles) implements StepEntity {

    public StepFillAreaStyle {
        styles = List.copyOf(styles);
    }
}
