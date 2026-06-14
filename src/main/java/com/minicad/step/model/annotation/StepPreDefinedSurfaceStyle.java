package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved PRE_DEFINED_SURFACE_STYLE.
 */
/**
 * Resolved PRE_DEFINED_SURFACE_STYLE.
 */
public final class StepPreDefinedSurfaceStyle implements StepEntity {
    private final int id;
    private final String name;
    private final String identifier;

    public StepPreDefinedSurfaceStyle(int id, String name, String identifier) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPreDefinedSurfaceStyle that = (StepPreDefinedSurfaceStyle) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, identifier);
    }

    @Override
    public String toString() {
        return "StepPreDefinedSurfaceStyle{" + "id=" + id + "name=" + name + "identifier=" + identifier + "}";
    }
}
