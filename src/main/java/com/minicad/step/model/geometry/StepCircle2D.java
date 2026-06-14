package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved CIRCLE_2D.
 * A circle in 2D parameter space.
 *
 * @param id step id
 * @param name step label
 * @param position 2D placement (center and direction)
 * @param radius circle radius
 */
/**
 * Resolved CIRCLE_2D.
 * A circle in 2D parameter space.
 *
 * @param id step id
 * @param name step label
 * @param position 2D placement (center and direction)
 * @param radius circle radius
 */
public final class StepCircle2D implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement2D position;
    private final double radius;

    public StepCircle2D(int id, String name, StepAxis2Placement2D position, double radius) {
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

    public StepAxis2Placement2D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCircle2D that = (StepCircle2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, radius);
    }

    @Override
    public String toString() {
        return "StepCircle2D{" + "id=" + id + "name=" + name + "position=" + position + "radius=" + radius + "}";
    }
}
