package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

/**
 * Resolved CHARACTER_GLYPH_STROKE.
 */
/**
 * Resolved CHARACTER_GLYPH_STROKE.
 */
public final class StepCharacterGlyphStroke implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity glyph;
    private final StepEntity stroke;

    public StepCharacterGlyphStroke(int id, String name, StepEntity glyph, StepEntity stroke) {
        this.id = id;
        this.name = name;
        this.glyph = glyph;
        this.stroke = stroke;
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

    public StepEntity getStroke() {
        return stroke;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCharacterGlyphStroke that = (StepCharacterGlyphStroke) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(glyph, that.glyph) && Objects.equals(stroke, that.stroke);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, glyph, stroke);
    }

    @Override
    public String toString() {
        return "StepCharacterGlyphStroke{" + "id=" + id + "name=" + name + "glyph=" + glyph + "stroke=" + stroke + "}";
    }
}
