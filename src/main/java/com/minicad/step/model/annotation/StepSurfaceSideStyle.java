package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal surface side style.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styles supported surface style components
 */
public record StepSurfaceSideStyle(int id, String name, List<StepEntity> styles) implements StepEntity {

    public StepSurfaceSideStyle {
        styles = List.copyOf(styles);
    }
}
