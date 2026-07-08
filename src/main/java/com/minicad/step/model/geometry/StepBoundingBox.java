package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved BOUNDING_BOX.
 * An axis-aligned bounding box.
 */
/**
 * Resolved BOUNDING_BOX.
 * An axis-aligned bounding box.
 */
public final class StepBoundingBox implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity corner1;
    private final StepEntity corner2;

    public StepBoundingBox(int id, String name, StepEntity corner1, StepEntity corner2) {
        this.id = id;
        this.name = name;
        this.corner1 = corner1;
        this.corner2 = corner2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCorner1() {
        return corner1;
    }

    public StepEntity getCorner2() {
        return corner2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBoundingBox that = (StepBoundingBox) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(corner1, that.corner1) && Objects.equals(corner2, that.corner2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, corner1, corner2);
    }

    @Override
    public String toString() {
        return "StepBoundingBox{" + "id=" + id + "name=" + name + "corner1=" + corner1 + "corner2=" + corner2 + "}";
    }
}
