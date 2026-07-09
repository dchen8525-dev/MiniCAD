package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved LINE_2D.
 *
 * @param id step id
 * @param name step label
 * @param point_2d point on the line
 * @param direction_2d direction of the line
 */
/**
 * Resolved LINE_2D.
 *
 * @param id step id
 * @param name step label
 * @param point_2d point on the line
 * @param direction_2d direction of the line
 */
public final class StepLine2D implements StepEntity {
    private final int id;
    private final String name;
    private final StepCartesianPoint point2d;
    private final StepDirection direction2d;

    public StepLine2D(int id, String name, StepCartesianPoint point2d, StepDirection direction2d) {
        this.id = id;
        this.name = name;
        this.point2d = point2d;
        this.direction2d = direction2d;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepCartesianPoint getPoint2d() {
        return point2d;
    }

    public StepDirection getDirection2d() {
        return direction2d;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCartesianPoint point2d() { return getPoint2d(); }
    public StepDirection direction2d() { return getDirection2d(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLine2D that = (StepLine2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(point2d, that.point2d) && Objects.equals(direction2d, that.direction2d);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, point2d, direction2d);
    }

    @Override
    public String toString() {
        return "StepLine2D{" + "id=" + id + "name=" + name + "point2d=" + point2d + "direction2d=" + direction2d + "}";
    }
}
