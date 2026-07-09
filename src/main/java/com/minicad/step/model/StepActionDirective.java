package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

public final class StepActionDirective implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final String directive;

    public StepActionDirective(int id, String name, String description, String directive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.directive = directive;
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

    public String getDirective() {
        return directive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepActionDirective that = (StepActionDirective) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(directive, that.directive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, directive);
    }

    @Override
    public String toString() {
        return "StepActionDirective{" + "id=" + id + "name=" + name + "description=" + description + "directive=" + directive + "}";
    }
}