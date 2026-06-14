package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

public final class StepAngularSize implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final double angle;

    public StepAngularSize(int id, String name, String description, double angle) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.angle = angle;
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

    public double getAngle() {
        return angle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAngularSize that = (StepAngularSize) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && angle == that.angle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, angle);
    }

    @Override
    public String toString() {
        return "StepAngularSize{" + "id=" + id + "name=" + name + "description=" + description + "angle=" + angle + "}";
    }
}