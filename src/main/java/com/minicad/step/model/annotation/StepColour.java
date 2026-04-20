package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal COLOUR marker.
 *
 * @param id step id
 */
public record StepColour(int id) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
