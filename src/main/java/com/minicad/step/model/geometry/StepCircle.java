package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved CIRCLE.
 *
 * @param id step id
 * @param name step label
 * @param position circle placement
 * @param radius radius value
 */
/**
 * Resolved CIRCLE.
 *
 * @param id step id
 * @param name step label
 * @param position circle placement
 * @param radius radius value
 */
public final class StepCircle implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final double radius;

    public StepCircle(int id, String name, StepEntity position, double radius) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCircle that = (StepCircle) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, radius);
    }

    @Override
    public String toString() {
        return "StepCircle{" + "id=" + id + "name=" + name + "position=" + position + "radius=" + radius + "}";
    }
}
