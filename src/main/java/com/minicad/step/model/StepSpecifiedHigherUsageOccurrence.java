package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved SPECIFIED_HIGHER_USAGE_OCCURRENCE.
 * Higher-level product usage occurrence.
 */
/**
 * Resolved SPECIFIED_HIGHER_USAGE_OCCURRENCE.
 * Higher-level product usage occurrence.
 */
public final class StepSpecifiedHigherUsageOccurrence implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity usage;

    public StepSpecifiedHigherUsageOccurrence(int id, String name, String description, StepEntity usage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.usage = usage;
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

    public StepEntity getUsage() {
        return usage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSpecifiedHigherUsageOccurrence that = (StepSpecifiedHigherUsageOccurrence) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(usage, that.usage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, usage);
    }

    @Override
    public String toString() {
        return "StepSpecifiedHigherUsageOccurrence{" + "id=" + id + "name=" + name + "description=" + description + "usage=" + usage + "}";
    }
}
