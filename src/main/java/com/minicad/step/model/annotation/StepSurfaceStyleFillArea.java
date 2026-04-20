package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal surface style fill area wrapper.
 *
 * @param id STEP instance id
 * @param fillStyle referenced fill style
 */
public record StepSurfaceStyleFillArea(int id, StepFillAreaStyle fillStyle) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
