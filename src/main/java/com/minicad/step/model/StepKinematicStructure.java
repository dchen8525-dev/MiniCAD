package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepKinematicStructure implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity mechanism;

    public StepKinematicStructure(int id, String name, String description, StepEntity mechanism) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mechanism = mechanism;
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

    public StepEntity getMechanism() {
        return mechanism;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKinematicStructure that = (StepKinematicStructure) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(mechanism, that.mechanism);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, mechanism);
    }

    @Override
    public String toString() {
        return "StepKinematicStructure{" + "id=" + id + "name=" + name + "description=" + description + "mechanism=" + mechanism + "}";
    }
}