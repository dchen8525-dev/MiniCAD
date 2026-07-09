package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved FILL_AREA_SHAPE_USE.
 * A fill area shape use entity.
 */
/**
 * Resolved FILL_AREA_SHAPE_USE.
 * A fill area shape use entity.
 */
public final class StepFillAreaShapeUse implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity fillArea;

    public StepFillAreaShapeUse(int id, String name, StepEntity fillArea) {
        this.id = id;
        this.name = name;
        this.fillArea = fillArea;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getFillArea() {
        return fillArea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFillAreaShapeUse that = (StepFillAreaShapeUse) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(fillArea, that.fillArea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fillArea);
    }

    @Override
    public String toString() {
        return "StepFillAreaShapeUse{" + "id=" + id + "name=" + name + "fillArea=" + fillArea + "}";
    }
}
