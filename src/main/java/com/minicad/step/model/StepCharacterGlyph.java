package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CHARACTER_GLYPH.
 */
public final class StepCharacterGlyph extends AbstractStepEntity {
    private final String characterCode;

    public StepCharacterGlyph(int id, String name, String characterCode) {
        super(id, name);
        this.characterCode = characterCode;
    }

    public String getCharacterCode() {
        return characterCode;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("characterCode", characterCode);
        return state;
    }
}
