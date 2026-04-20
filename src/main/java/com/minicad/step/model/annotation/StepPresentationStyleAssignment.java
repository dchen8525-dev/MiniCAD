package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
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
