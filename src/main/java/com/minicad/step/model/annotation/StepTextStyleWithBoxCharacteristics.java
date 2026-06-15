package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal TEXT_STYLE_WITH_BOX_CHARACTERISTICS.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param boxCharacteristics raw box characteristic literals
 */
/**
 * Minimal TEXT_STYLE_WITH_BOX_CHARACTERISTICS.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param boxCharacteristics raw box characteristic literals
 */
public final class StepTextStyleWithBoxCharacteristics implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity characterAppearance;
    private final List<String> boxCharacteristics;

    public StepTextStyleWithBoxCharacteristics(int id, String name, StepEntity characterAppearance, List<String> boxCharacteristics) {
        this.id = id;
        this.name = name;
        this.characterAppearance = characterAppearance;
        this.boxCharacteristics = boxCharacteristics == null ? null : java.util.List.copyOf(boxCharacteristics);
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

    public List<String> getBoxCharacteristics() {
        return boxCharacteristics;
    }

    // Record-style accessor
    public StepEntity characterAppearance() {
        return characterAppearance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTextStyleWithBoxCharacteristics that = (StepTextStyleWithBoxCharacteristics) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(characterAppearance, that.characterAppearance) && Objects.equals(boxCharacteristics, that.boxCharacteristics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, characterAppearance, boxCharacteristics);
    }

    @Override
    public String toString() {
        return "StepTextStyleWithBoxCharacteristics{" + "id=" + id + "name=" + name + "characterAppearance=" + characterAppearance + "boxCharacteristics=" + boxCharacteristics + "}";
    }
}
