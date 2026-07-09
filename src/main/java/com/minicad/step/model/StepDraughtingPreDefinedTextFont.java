package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal DRAUGHTING_PRE_DEFINED_TEXT_FONT.
 *
 * @param id step id
 * @param name predefined draughting text font name
 */
/**
 * Minimal DRAUGHTING_PRE_DEFINED_TEXT_FONT.
 *
 * @param id step id
 * @param name predefined draughting text font name
 */
public final class StepDraughtingPreDefinedTextFont implements StepEntity {
    private final int id;
    private final String name;

    public StepDraughtingPreDefinedTextFont(int id, String name) {
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
        StepDraughtingPreDefinedTextFont that = (StepDraughtingPreDefinedTextFont) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepDraughtingPreDefinedTextFont{" + "id=" + id + "name=" + name + "}";
    }
}
