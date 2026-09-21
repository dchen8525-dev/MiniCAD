package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal TEXT_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 */
public final class StepTextStyle extends AbstractStepEntity {
    private final StepEntity characterAppearance;

    public StepTextStyle(int id, String name, StepEntity characterAppearance) {
        super(id, name);
        this.characterAppearance = characterAppearance;
    }

    public StepEntity getCharacterAppearance() {
        return characterAppearance;
    }

    // Record-style accessor
    public StepEntity characterAppearance() {
        return characterAppearance;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("characterAppearance", characterAppearance);
        return state;
    }
}
