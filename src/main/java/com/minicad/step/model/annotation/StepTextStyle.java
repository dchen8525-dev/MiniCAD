package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal TEXT_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 */
/**
 * Minimal TEXT_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 */
public final class StepTextStyle implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity characterAppearance;

    public StepTextStyle(int id, String name, StepEntity characterAppearance) {
        this.id = id;
        this.name = name;
        this.characterAppearance = characterAppearance;
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

    // Record-style accessor
    public StepEntity characterAppearance() {
        return characterAppearance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTextStyle that = (StepTextStyle) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(characterAppearance, that.characterAppearance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, characterAppearance);
    }

    @Override
    public String toString() {
        return "StepTextStyle{" + "id=" + id + "name=" + name + "characterAppearance=" + characterAppearance + "}";
    }
}
