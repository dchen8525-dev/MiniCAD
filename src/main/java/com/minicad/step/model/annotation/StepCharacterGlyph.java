package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved CHARACTER_GLYPH.
 */
/**
 * Resolved CHARACTER_GLYPH.
 */
public final class StepCharacterGlyph implements StepEntity {
    private final int id;
    private final String name;
    private final String characterCode;

    public StepCharacterGlyph(int id, String name, String characterCode) {
        this.id = id;
        this.name = name;
        this.characterCode = characterCode;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCharacterCode() {
        return characterCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCharacterGlyph that = (StepCharacterGlyph) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(characterCode, that.characterCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, characterCode);
    }

    @Override
    public String toString() {
        return "StepCharacterGlyph{" + "id=" + id + "name=" + name + "characterCode=" + characterCode + "}";
    }
}
