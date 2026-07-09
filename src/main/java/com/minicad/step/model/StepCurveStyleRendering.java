package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved CURVE_STYLE_RENDERING.
 */
/**
 * Resolved CURVE_STYLE_RENDERING.
 */
public final class StepCurveStyleRendering implements StepEntity {
    private final int id;
    private final String name;
    private final double transparency;
    private final StepEntity colour;

    public StepCurveStyleRendering(int id, String name, double transparency, StepEntity colour) {
        this.id = id;
        this.name = name;
        this.transparency = transparency;
        this.colour = colour;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTransparency() {
        return transparency;
    }

    public StepEntity getColour() {
        return colour;
    }

    // Record-style accessors
    public String name() {
        return name;
    }

    public double transparency() {
        return transparency;
    }

    public StepEntity colour() {
        return colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCurveStyleRendering that = (StepCurveStyleRendering) o;
        return id == that.id && Objects.equals(name, that.name) && transparency == that.transparency && Objects.equals(colour, that.colour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transparency, colour);
    }

    @Override
    public String toString() {
        return "StepCurveStyleRendering{" + "id=" + id + "name=" + name + "transparency=" + transparency + "colour=" + colour + "}";
    }
}
