package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved PRISM_VOLUME.
 * A CSG prism (wedge) primitive volume.
 */
/**
 * Resolved PRISM_VOLUME.
 * A CSG prism (wedge) primitive volume.
 */
public final class StepPrismVolume implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final Double width;
    private final Double depth;
    private final Double height;

    public StepPrismVolume(int id, String name, StepEntity position, Double width, Double depth, Double height) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.width = width;
        this.depth = depth;
        this.height = height;
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

    public Double getWidth() {
        return width;
    }

    public Double getDepth() {
        return depth;
    }

    public Double getHeight() {
        return height;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public Double width() { return getWidth(); }
    public Double depth() { return getDepth(); }
    public Double height() { return getHeight(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPrismVolume that = (StepPrismVolume) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(width, that.width) && Objects.equals(depth, that.depth) && Objects.equals(height, that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, width, depth, height);
    }

    @Override
    public String toString() {
        return "StepPrismVolume{" + "id=" + id + "name=" + name + "position=" + position + "width=" + width + "depth=" + depth + "height=" + height + "}";
    }
}
