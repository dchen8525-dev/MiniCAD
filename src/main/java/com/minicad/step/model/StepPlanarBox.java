package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepPlanarBox implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity placement;
    private final double width;
    private final double height;

    public StepPlanarBox(int id, String name, StepEntity placement, double width, double height) {
        this.id = id;
        this.name = name;
        this.placement = placement;
        this.width = width;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPlacement() {
        return placement;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    // Record-style accessors
    public StepEntity placement() {
        return placement;
    }

    public double width() {
        return width;
    }

    public double height() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPlanarBox that = (StepPlanarBox) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(placement, that.placement) && width == that.width && height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, placement, width, height);
    }

    @Override
    public String toString() {
        return "StepPlanarBox{" + "id=" + id + "name=" + name + "placement=" + placement + "width=" + width + "height=" + height + "}";
    }
}