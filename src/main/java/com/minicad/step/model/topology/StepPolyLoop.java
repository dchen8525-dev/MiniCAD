package com.minicad.step.model.topology;

import java.util.List;

import com.minicad.step.model.geometry.StepCartesianPoint;
import java.util.Objects;

/**
 * Resolved POLY_LOOP.
 *
 * @param id STEP instance id
 * @param name loop name
 * @param polygon polygon points
 */
/**
 * Resolved POLY_LOOP.
 *
 * @param id STEP instance id
 * @param name loop name
 * @param polygon polygon points
 */
public final class StepPolyLoop implements StepLoop {
    private final int id;
    private final String name;
    private final List<StepCartesianPoint> polygon;

    public StepPolyLoop(int id, String name, List<StepCartesianPoint> polygon) {
        this.id = id;
        this.name = name;
        this.polygon = polygon == null ? null : java.util.List.copyOf(polygon);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepCartesianPoint> getPolygon() {
        return polygon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPolyLoop that = (StepPolyLoop) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(polygon, that.polygon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, polygon);
    }

    @Override
    public String toString() {
        return "StepPolyLoop{" + "id=" + id + "name=" + name + "polygon=" + polygon + "}";
    }
}
