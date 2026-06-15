package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal annotation fill area geometry.
 *
 * @param id STEP instance id
 * @param name representation item name
 * @param boundaries fill boundaries
 */
/**
 * Minimal annotation fill area geometry.
 *
 * @param id STEP instance id
 * @param name representation item name
 * @param boundaries fill boundaries
 */
public final class StepAnnotationFillArea implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> boundaries;

    public StepAnnotationFillArea(int id, String name, List<StepEntity> boundaries) {
        this.id = id;
        this.name = name;
        this.boundaries = boundaries == null ? null : java.util.List.copyOf(boundaries);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getBoundaries() {
        return boundaries;
    }

    // Record-style accessor
    public List<StepEntity> boundaries() {
        return boundaries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnnotationFillArea that = (StepAnnotationFillArea) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(boundaries, that.boundaries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, boundaries);
    }

    @Override
    public String toString() {
        return "StepAnnotationFillArea{" + "id=" + id + "name=" + name + "boundaries=" + boundaries + "}";
    }
}
