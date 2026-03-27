package com.minicad.step.model;

import java.util.List;

/**
 * Minimal surface side style.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styles supported surface style components
 */
public record StepSurfaceSideStyle(int id, String name, List<StepSurfaceStyleFillArea> styles) implements StepEntity {

    public StepSurfaceSideStyle {
        styles = List.copyOf(styles);
    }
}
