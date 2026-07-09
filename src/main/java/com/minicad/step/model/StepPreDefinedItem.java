package com.minicad.step.model;

import java.util.Objects;

/**
 * Minimal PRE_DEFINED_ITEM.
 *
 * @param id step id
 * @param name predefined item name
 */
/**
 * Minimal PRE_DEFINED_ITEM.
 *
 * @param id step id
 * @param name predefined item name
 */
public final class StepPreDefinedItem implements StepEntity {
    private final int id;
    private final String name;

    public StepPreDefinedItem(int id, String name) {
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
        StepPreDefinedItem that = (StepPreDefinedItem) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepPreDefinedItem{" + "id=" + id + "name=" + name + "}";
    }
}
