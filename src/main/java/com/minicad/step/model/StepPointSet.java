package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal point set.
 *
 * @param id STEP instance id
 * @param name set name
 * @param points point elements
 */
/**
 * Minimal point set.
 *
 * @param id STEP instance id
 * @param name set name
 * @param points point elements
 */
public final class StepPointSet implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> points;

    public StepPointSet(int id, String name, List<StepEntity> points) {
        this.id = id;
        this.name = name;
        this.points = points == null ? null : java.util.List.copyOf(points);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getPoints() {
        return points;
    }

    // Record-style accessor
    public List<StepEntity> points() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPointSet that = (StepPointSet) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, points);
    }

    @Override
    public String toString() {
        return "StepPointSet{" + "id=" + id + "name=" + name + "points=" + points + "}";
    }
}
