package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal TEXT_STYLE_WITH_SPACING.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param characterSpacing additional spacing between characters
 */
/**
 * Minimal TEXT_STYLE_WITH_SPACING.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param characterSpacing additional spacing between characters
 */
public final class StepTextStyleWithSpacing implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity characterAppearance;
    private final double characterSpacing;

    public StepTextStyleWithSpacing(int id, String name, StepEntity characterAppearance, double characterSpacing) {
        this.id = id;
        this.name = name;
        this.characterAppearance = characterAppearance;
        this.characterSpacing = characterSpacing;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTextStyleWithSpacing that = (StepTextStyleWithSpacing) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(characterAppearance, that.characterAppearance) && characterSpacing == that.characterSpacing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, characterAppearance, characterSpacing);
    }

    @Override
    public String toString() {
        return "StepTextStyleWithSpacing{" + "id=" + id + "name=" + name + "characterAppearance=" + characterAppearance + "characterSpacing=" + characterSpacing + "}";
    }
}
