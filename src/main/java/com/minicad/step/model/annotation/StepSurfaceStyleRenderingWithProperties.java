package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;import java.util.List;
import java.util.Objects;

public final class StepSurfaceStyleRenderingWithProperties implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> properties;

    public StepSurfaceStyleRenderingWithProperties(int id, String name, List<StepEntity> properties) {
        this.id = id;
        this.name = name;
        this.properties = properties == null ? null : java.util.List.copyOf(properties);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceStyleRenderingWithProperties that = (StepSurfaceStyleRenderingWithProperties) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, properties);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleRenderingWithProperties{" + "id=" + id + "name=" + name + "properties=" + properties + "}";
    }
}