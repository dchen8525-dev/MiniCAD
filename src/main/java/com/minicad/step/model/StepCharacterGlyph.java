package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "characterCode", characterCode);
    }
}
