package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved CHARACTER_GLYPH_OUTLINE.
 */
/**
 * Resolved CHARACTER_GLYPH_OUTLINE.
 */
public final class StepCharacterGlyphOutline implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity glyph;
    private final StepEntity outline;

    public StepCharacterGlyphOutline(int id, String name, StepEntity glyph, StepEntity outline) {
        this.id = id;
        this.name = name;
        this.glyph = glyph;
        this.outline = outline;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCharacterGlyphOutline that = (StepCharacterGlyphOutline) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(glyph, that.glyph) && Objects.equals(outline, that.outline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, glyph, outline);
    }

    @Override
    public String toString() {
        return "StepCharacterGlyphOutline{" + "id=" + id + "name=" + name + "glyph=" + glyph + "outline=" + outline + "}";
    }
}
