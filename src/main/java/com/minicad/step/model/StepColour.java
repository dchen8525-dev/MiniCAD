package com.minicad.step.model;

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
