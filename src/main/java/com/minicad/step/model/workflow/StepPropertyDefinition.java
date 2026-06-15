package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal property definition metadata.
 *
 * @param id STEP instance id
 * @param name property name
 * @param description property description
 * @param definition related semantic target
 */
/**
 * Minimal property definition metadata.
 *
 * @param id STEP instance id
 * @param name property name
 * @param description property description
 * @param definition related semantic target
 */
public final class StepPropertyDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity definition;

    public StepPropertyDefinition(int id, String name, String description, StepEntity definition) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.definition = definition;
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

    // Record-style accessor
    public StepEntity definition() {
        return definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPropertyDefinition that = (StepPropertyDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(definition, that.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, definition);
    }

    @Override
    public String toString() {
        return "StepPropertyDefinition{" + "id=" + id + "name=" + name + "description=" + description + "definition=" + definition + "}";
    }
}
