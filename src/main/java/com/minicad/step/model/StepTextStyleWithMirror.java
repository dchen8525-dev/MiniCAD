package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal TEXT_STYLE_WITH_MIRROR.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param mirrorPlacement mirror axis placement
 */
public final class StepTextStyleWithMirror extends AbstractStepEntity {
    private final StepEntity characterAppearance;
    private final StepEntity mirrorPlacement;

    public StepTextStyleWithMirror(int id, String name, StepEntity characterAppearance, StepEntity mirrorPlacement) {
        super(id, name);
        this.characterAppearance = characterAppearance;
        this.mirrorPlacement = mirrorPlacement;
    }

    public StepEntity getCharacterAppearance() {
        return characterAppearance;
    }

    public StepEntity getMirrorPlacement() {
        return mirrorPlacement;
    }

    // Record-style accessors
    public StepEntity characterAppearance() {
        return characterAppearance;
    }

    public StepEntity mirrorPlacement() {
        return mirrorPlacement;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "characterAppearance", characterAppearance,
                "mirrorPlacement", mirrorPlacement);
    }
}
