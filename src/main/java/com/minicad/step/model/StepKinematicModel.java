package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved KINEMATIC_MODEL.
 * A kinematic model containing links and joints.
 */
/**
 * Resolved KINEMATIC_MODEL.
 * A kinematic model containing links and joints.
 */
public final class StepKinematicModel implements StepEntity {
    private final int id;
    private final String name;
    private final String description;

    public StepKinematicModel(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKinematicModel that = (StepKinematicModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "StepKinematicModel{" + "id=" + id + "name=" + name + "description=" + description + "}";
    }
}
