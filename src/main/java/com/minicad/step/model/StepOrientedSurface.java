package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal ORIENTED_SURFACE parse-only surface wrapper.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param surfaceElement referenced surface
 * @param orientation orientation sense
 */
/**
 * Minimal ORIENTED_SURFACE parse-only surface wrapper.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param surfaceElement referenced surface
 * @param orientation orientation sense
 */
public final class StepOrientedSurface implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity surfaceElement;
    private final boolean orientation;

    public StepOrientedSurface(int id, String name, StepEntity surfaceElement, boolean orientation) {
        this.id = id;
        this.name = name;
        this.surfaceElement = surfaceElement;
        this.orientation = orientation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSurfaceElement() {
        return surfaceElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity surfaceElement() { return getSurfaceElement(); }
    public boolean orientation() { return isOrientation(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrientedSurface that = (StepOrientedSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(surfaceElement, that.surfaceElement) && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surfaceElement, orientation);
    }

    @Override
    public String toString() {
        return "StepOrientedSurface{" + "id=" + id + "name=" + name + "surfaceElement=" + surfaceElement + "orientation=" + orientation + "}";
    }
}
