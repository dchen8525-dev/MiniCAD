package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

public final class StepPlanarExtent implements StepEntity {
    private final int id;
    private final String name;
    private final double width;
    private final double height;

    public StepPlanarExtent(int id, String name, double width, double height) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPlanarExtent that = (StepPlanarExtent) o;
        return id == that.id && Objects.equals(name, that.name) && width == that.width && height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, width, height);
    }

    @Override
    public String toString() {
        return "StepPlanarExtent{" + "id=" + id + "name=" + name + "width=" + width + "height=" + height + "}";
    }
}