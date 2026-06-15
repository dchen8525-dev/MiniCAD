package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS.
 *
 * @param id STEP instance id
 * @param outlineStyle referenced curve style
 * @param characteristics referenced fill area style
 */
/**
 * Minimal CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS.
 *
 * @param id STEP instance id
 * @param outlineStyle referenced curve style
 * @param characteristics referenced fill area style
 */
public final class StepCharacterGlyphStyleOutlineWithCharacteristics implements StepEntity {
    private final int id;
    private final StepCurveStyle outlineStyle;
    private final StepFillAreaStyle characteristics;

    public StepCharacterGlyphStyleOutlineWithCharacteristics(int id, StepCurveStyle outlineStyle, StepFillAreaStyle characteristics) {
        this.id = id;
        this.outlineStyle = outlineStyle;
        this.characteristics = characteristics;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepCurveStyle getOutlineStyle() {
        return outlineStyle;
    }

    public StepFillAreaStyle getCharacteristics() {
        return characteristics;
    }

    // Record-style accessors
    public StepCurveStyle outlineStyle() {
        return outlineStyle;
    }

    public StepFillAreaStyle characteristics() {
        return characteristics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCharacterGlyphStyleOutlineWithCharacteristics that = (StepCharacterGlyphStyleOutlineWithCharacteristics) o;
        return id == that.id && Objects.equals(outlineStyle, that.outlineStyle) && Objects.equals(characteristics, that.characteristics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, outlineStyle, characteristics);
    }

    @Override
    public String toString() {
        return "StepCharacterGlyphStyleOutlineWithCharacteristics{" + "id=" + id + "outlineStyle=" + outlineStyle + "characteristics=" + characteristics + "}";
    }
}
