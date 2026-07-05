package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal product definition shape.
 *
 * @param id STEP instance id
 * @param name shape name
 * @param description optional description
 * @param definition referenced product definition or product definition relationship (may be null for profile definitions)
 */
public final class StepProductDefinitionShape implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity definition;  // May be null for profile definitions in complex entities

    public StepProductDefinitionShape(int id, String name, String description, StepEntity definition) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.definition = definition;  // Accepts null for omitted parameter
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getDefinition() {
        return definition;
    }

    // Record-style accessors
    public int id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public StepEntity definition() { return definition; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductDefinitionShape that = (StepProductDefinitionShape) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(definition, that.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, definition);
    }

    @Override
    public String toString() {
        return "StepProductDefinitionShape{" + "id=" + id + "name=" + name + "description=" + description + "definition=" + definition + "}";
    }
}
