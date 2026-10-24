package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal TEXT_STYLE_WITH_SPACING.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param characterSpacing additional spacing between characters
 */
public final class StepTextStyleWithSpacing extends AbstractStepEntity {
    private final StepEntity characterAppearance;
    private final double characterSpacing;

    public StepTextStyleWithSpacing(int id, String name, StepEntity characterAppearance, double characterSpacing) {
        super(id, name);
        this.characterAppearance = characterAppearance;
        this.characterSpacing = characterSpacing;
    }

    public StepEntity getCharacterAppearance() {
        return characterAppearance;
    }

    public double getCharacterSpacing() {
        return characterSpacing;
    }

    // Record-style accessor
    public StepEntity characterAppearance() {
        return characterAppearance;
    }

    public double characterSpacing() {
        return characterSpacing;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "characterAppearance", characterAppearance,
                "characterSpacing", characterSpacing);
    }
}
