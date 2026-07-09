package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal PRE_DEFINED_TEXT_FONT.
 *
 * @param id step id
 * @param name predefined text font name
 */
/**
 * Minimal PRE_DEFINED_TEXT_FONT.
 *
 * @param id step id
 * @param name predefined text font name
 */
public final class StepPreDefinedTextFont implements StepEntity {
    private final int id;
    private final String name;

    public StepPreDefinedTextFont(int id, String name) {
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
        StepPreDefinedTextFont that = (StepPreDefinedTextFont) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepPreDefinedTextFont{" + "id=" + id + "name=" + name + "}";
    }
}
