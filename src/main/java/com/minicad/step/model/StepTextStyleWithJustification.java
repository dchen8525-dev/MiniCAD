package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal TEXT_STYLE_WITH_JUSTIFICATION.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param justification justification token
 */
public final class StepTextStyleWithJustification extends AbstractStepEntity {
    private final StepEntity characterAppearance;
    private final String justification;

    public StepTextStyleWithJustification(int id, String name, StepEntity characterAppearance, String justification) {
        super(id, name);
        this.characterAppearance = characterAppearance;
        this.justification = justification;
    }

    public StepEntity getCharacterAppearance() {
        return characterAppearance;
    }

    public String getJustification() {
        return justification;
    }

    // Record-style accessor
    public StepEntity characterAppearance() {
        return characterAppearance;
    }

    public String justification() {
        return justification;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("characterAppearance", characterAppearance);
        state.put("justification", justification);
        return state;
    }
}
