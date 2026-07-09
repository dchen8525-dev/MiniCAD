package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CURVE_STYLE.
 *
 * @param id step id
 * @param name style name
 * @param curveFont referenced font
 * @param curveWidth stroke width
 * @param colour referenced colour
 */
/**
 * Minimal CURVE_STYLE.
 *
 * @param id step id
 * @param name style name
 * @param curveFont referenced font
 * @param curveWidth stroke width
 * @param colour referenced colour
 */
public final class StepCurveStyle implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity curveFont;
    private final double curveWidth;
    private final StepEntity colour;

    public StepCurveStyle(int id, String name, StepEntity curveFont, double curveWidth, StepEntity colour) {
        this.id = id;
        this.name = name;
        this.curveFont = curveFont;
        this.curveWidth = curveWidth;
        this.colour = colour;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCurveFont() {
        return curveFont;
    }

    public double getCurveWidth() {
        return curveWidth;
    }

    public StepEntity getColour() {
        return colour;
    }

    // Record-style accessors
    public StepEntity curveFont() {
        return curveFont;
    }

    public double curveWidth() {
        return curveWidth;
    }

    public StepEntity colour() {
        return colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCurveStyle that = (StepCurveStyle) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(curveFont, that.curveFont) && curveWidth == that.curveWidth && Objects.equals(colour, that.colour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, curveFont, curveWidth, colour);
    }

    @Override
    public String toString() {
        return "StepCurveStyle{" + "id=" + id + "name=" + name + "curveFont=" + curveFont + "curveWidth=" + curveWidth + "colour=" + colour + "}";
    }
}
