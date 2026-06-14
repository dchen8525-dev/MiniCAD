package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal PRE_DEFINED_CURVE_FONT.
 *
 * @param id step id
 * @param name predefined font name
 */
/**
 * Minimal PRE_DEFINED_CURVE_FONT.
 *
 * @param id step id
 * @param name predefined font name
 */
public final class StepPreDefinedCurveFont implements StepEntity {
    private final int id;
    private final String name;

    public StepPreDefinedCurveFont(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPreDefinedCurveFont that = (StepPreDefinedCurveFont) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepPreDefinedCurveFont{" + "id=" + id + "name=" + name + "}";
    }
}
