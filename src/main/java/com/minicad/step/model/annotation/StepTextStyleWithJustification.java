package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal TEXT_STYLE_WITH_JUSTIFICATION.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param justification justification token
 */
/**
 * Minimal TEXT_STYLE_WITH_JUSTIFICATION.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param justification justification token
 */
public final class StepTextStyleWithJustification implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity characterAppearance;
    private final String justification;

    public StepTextStyleWithJustification(int id, String name, StepEntity characterAppearance, String justification) {
        this.id = id;
        this.name = name;
        this.characterAppearance = characterAppearance;
        this.justification = justification;
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

    public String getJustification() {
        return justification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTextStyleWithJustification that = (StepTextStyleWithJustification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(characterAppearance, that.characterAppearance) && Objects.equals(justification, that.justification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, characterAppearance, justification);
    }

    @Override
    public String toString() {
        return "StepTextStyleWithJustification{" + "id=" + id + "name=" + name + "characterAppearance=" + characterAppearance + "justification=" + justification + "}";
    }
}
