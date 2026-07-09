package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CHARACTER_GLYPH_STYLE_STROKE.
 *
 * @param id STEP instance id
 * @param strokeStyle referenced curve style
 */
/**
 * Minimal CHARACTER_GLYPH_STYLE_STROKE.
 *
 * @param id STEP instance id
 * @param strokeStyle referenced curve style
 */
public final class StepCharacterGlyphStyleStroke implements StepEntity {
    private final int id;
    private final StepCurveStyle strokeStyle;

    public StepCharacterGlyphStyleStroke(int id, StepCurveStyle strokeStyle) {
        this.id = id;
        this.strokeStyle = strokeStyle;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepCurveStyle getStrokeStyle() {
        return strokeStyle;
    }

    // Record-style accessor
    public StepCurveStyle strokeStyle() {
        return strokeStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCharacterGlyphStyleStroke that = (StepCharacterGlyphStyleStroke) o;
        return id == that.id && Objects.equals(strokeStyle, that.strokeStyle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, strokeStyle);
    }

    @Override
    public String toString() {
        return "StepCharacterGlyphStyleStroke{" + "id=" + id + "strokeStyle=" + strokeStyle + "}";
    }
}
