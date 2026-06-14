package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved CURVE_STYLE_WITH_FONT.
 */
/**
 * Resolved CURVE_STYLE_WITH_FONT.
 */
public final class StepCurveStyleWithFont implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity font;
    private final double width;

    public StepCurveStyleWithFont(int id, String name, StepEntity font, double width) {
        this.id = id;
        this.name = name;
        this.font = font;
        this.width = width;
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

    public double getWidth() {
        return width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCurveStyleWithFont that = (StepCurveStyleWithFont) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(font, that.font) && width == that.width;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, font, width);
    }

    @Override
    public String toString() {
        return "StepCurveStyleWithFont{" + "id=" + id + "name=" + name + "font=" + font + "width=" + width + "}";
    }
}
