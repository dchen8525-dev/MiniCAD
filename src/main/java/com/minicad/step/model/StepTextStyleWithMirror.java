package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal TEXT_STYLE_WITH_MIRROR.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param mirrorPlacement mirror axis placement
 */
/**
 * Minimal TEXT_STYLE_WITH_MIRROR.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param mirrorPlacement mirror axis placement
 */
public final class StepTextStyleWithMirror implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity characterAppearance;
    private final StepEntity mirrorPlacement;

    public StepTextStyleWithMirror(int id, String name, StepEntity characterAppearance, StepEntity mirrorPlacement) {
        this.id = id;
        this.name = name;
        this.characterAppearance = characterAppearance;
        this.mirrorPlacement = mirrorPlacement;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTextStyleWithMirror that = (StepTextStyleWithMirror) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(characterAppearance, that.characterAppearance) && Objects.equals(mirrorPlacement, that.mirrorPlacement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, characterAppearance, mirrorPlacement);
    }

    @Override
    public String toString() {
        return "StepTextStyleWithMirror{" + "id=" + id + "name=" + name + "characterAppearance=" + characterAppearance + "mirrorPlacement=" + mirrorPlacement + "}";
    }
}
