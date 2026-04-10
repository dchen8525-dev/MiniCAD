package com.minicad.step.model;

/**
 * Minimal TEXT_STYLE_FOR_DEFINED_FONT.
 *
 * @param id STEP instance id
 * @param textColour referenced text colour
 */
public record StepTextStyleForDefinedFont(int id, StepEntity textColour) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
