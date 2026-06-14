package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

public final class StepKinematicLink implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity shape;

    public StepKinematicLink(int id, String name, String description, StepEntity shape) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shape = shape;
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

    public StepEntity getShape() {
        return shape;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKinematicLink that = (StepKinematicLink) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(shape, that.shape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, shape);
    }

    @Override
    public String toString() {
        return "StepKinematicLink{" + "id=" + id + "name=" + name + "description=" + description + "shape=" + shape + "}";
    }
}