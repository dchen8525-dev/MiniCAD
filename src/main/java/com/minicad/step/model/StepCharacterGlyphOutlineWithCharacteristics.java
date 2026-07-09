package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved CHARACTER_GLYPH_OUTLINE_WITH_CHARACTERISTICS.
 */
/**
 * Resolved CHARACTER_GLYPH_OUTLINE_WITH_CHARACTERISTICS.
 */
public final class StepCharacterGlyphOutlineWithCharacteristics implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity glyph;
    private final StepEntity outline;
    private final StepEntity characteristics;

    public StepCharacterGlyphOutlineWithCharacteristics(int id, String name, StepEntity glyph, StepEntity outline, StepEntity characteristics) {
        this.id = id;
        this.name = name;
        this.glyph = glyph;
        this.outline = outline;
        this.characteristics = characteristics;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getGlyph() {
        return glyph;
    }

    public StepEntity getOutline() {
        return outline;
    }

    public StepEntity getCharacteristics() {
        return characteristics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCharacterGlyphOutlineWithCharacteristics that = (StepCharacterGlyphOutlineWithCharacteristics) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(glyph, that.glyph) && Objects.equals(outline, that.outline) && Objects.equals(characteristics, that.characteristics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, glyph, outline, characteristics);
    }

    @Override
    public String toString() {
        return "StepCharacterGlyphOutlineWithCharacteristics{" + "id=" + id + "name=" + name + "glyph=" + glyph + "outline=" + outline + "characteristics=" + characteristics + "}";
    }
}
