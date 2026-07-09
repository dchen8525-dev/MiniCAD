package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal DRAUGHTING_PRE_DEFINED_CURVE_FONT.
 *
 * @param id step id
 * @param name predefined font name
 */
/**
 * Minimal DRAUGHTING_PRE_DEFINED_CURVE_FONT.
 *
 * @param id step id
 * @param name predefined font name
 */
public final class StepDraughtingPreDefinedCurveFont implements StepEntity {
    private final int id;
    private final String name;

    public StepDraughtingPreDefinedCurveFont(int id, String name) {
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
        StepDraughtingPreDefinedCurveFont that = (StepDraughtingPreDefinedCurveFont) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepDraughtingPreDefinedCurveFont{" + "id=" + id + "name=" + name + "}";
    }
}
