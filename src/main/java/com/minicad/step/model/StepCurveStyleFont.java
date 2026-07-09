package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

/**
 * Resolved CURVE_STYLE_FONT.
 */
/**
 * Resolved CURVE_STYLE_FONT.
 */
public final class StepCurveStyleFont implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity font;

    public StepCurveStyleFont(int id, String name, StepEntity font) {
        this.id = id;
        this.name = name;
        this.font = font;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getFont() {
        return font;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCurveStyleFont that = (StepCurveStyleFont) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(font, that.font);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, font);
    }

    @Override
    public String toString() {
        return "StepCurveStyleFont{" + "id=" + id + "name=" + name + "font=" + font + "}";
    }
}
