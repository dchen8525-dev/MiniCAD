package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal surface style usage.
 *
 * @param id STEP instance id
 * @param side side enum
 * @param style referenced side style
 */
public record StepSurfaceStyleUsage(int id, String side, StepSurfaceSideStyle style) implements StepEntity {

    @Override
    public String name() {
        return side;
    }
}
