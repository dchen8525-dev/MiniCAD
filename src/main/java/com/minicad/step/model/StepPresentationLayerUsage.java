package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepPresentationLayerUsage implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity layer;

    public StepPresentationLayerUsage(int id, String name, String description, StepEntity layer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.layer = layer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getLayer() {
        return layer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPresentationLayerUsage that = (StepPresentationLayerUsage) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(layer, that.layer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, layer);
    }

    @Override
    public String toString() {
        return "StepPresentationLayerUsage{" + "id=" + id + "name=" + name + "description=" + description + "layer=" + layer + "}";
    }
}