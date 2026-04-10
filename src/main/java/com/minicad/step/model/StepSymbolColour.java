package com.minicad.step.model;

/**
 * Minimal SYMBOL_COLOUR.
 *
 * @param id STEP instance id
 * @param colour referenced colour
 */
public record StepSymbolColour(int id, StepEntity colour) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
