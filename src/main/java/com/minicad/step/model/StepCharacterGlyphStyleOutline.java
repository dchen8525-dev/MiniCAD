package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CHARACTER_GLYPH_STYLE_OUTLINE.
 *
 * @param id STEP instance id
 * @param outlineStyle referenced curve style
 */
/**
 * Minimal CHARACTER_GLYPH_STYLE_OUTLINE.
 *
 * @param id STEP instance id
 * @param outlineStyle referenced curve style
 */
public final class StepCharacterGlyphStyleOutline implements StepEntity {
    private final int id;
    private final StepCurveStyle outlineStyle;

    public StepCharacterGlyphStyleOutline(int id, StepCurveStyle outlineStyle) {
        this.id = id;
        this.outlineStyle = outlineStyle;
    }

    public int getId() {
        return id;
    }

    public StepCurveStyle getOutlineStyle() {
        return outlineStyle;
    }

    public String getName() {
        return "";
    }

    // Record-style accessor - no name field, return empty string
    public String name() {
        return "";
    }

    public StepCurveStyle outlineStyle() {
        return outlineStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCharacterGlyphStyleOutline that = (StepCharacterGlyphStyleOutline) o;
        return id == that.id && Objects.equals(outlineStyle, that.outlineStyle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, outlineStyle);
    }

    @Override
    public String toString() {
        return "StepCharacterGlyphStyleOutline{" + "id=" + id + "outlineStyle=" + outlineStyle + "}";
    }
}
