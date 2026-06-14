package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved PARABOLA 2D.
 *
 * @param id step id
 * @param name step label
 * @param position placement of the parabola
 * @param focalDist focal distance of the parabola
 */
/**
 * Resolved PARABOLA 2D.
 *
 * @param id step id
 * @param name step label
 * @param position placement of the parabola
 * @param focalDist focal distance of the parabola
 */
public final class StepParabola2D implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement2D position;
    private final double focalDist;

    public StepParabola2D(int id, String name, StepAxis2Placement2D position, double focalDist) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.focalDist = focalDist;
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

    public double getFocalDist() {
        return focalDist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepParabola2D that = (StepParabola2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && focalDist == that.focalDist;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, focalDist);
    }

    @Override
    public String toString() {
        return "StepParabola2D{" + "id=" + id + "name=" + name + "position=" + position + "focalDist=" + focalDist + "}";
    }
}
