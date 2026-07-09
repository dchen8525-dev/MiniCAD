package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;


public final class StepFeaVolumeElementProperty implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> properties;
    private final StepEntity material;

    public StepFeaVolumeElementProperty(int id, String name, List<StepEntity> properties, StepEntity material) {
        this.id = id;
        this.name = name;
        this.properties = properties == null ? null : java.util.List.copyOf(properties);
        this.material = material;
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

    public StepEntity getMaterial() {
        return material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaVolumeElementProperty that = (StepFeaVolumeElementProperty) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(properties, that.properties) && Objects.equals(material, that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, properties, material);
    }

    @Override
    public String toString() {
        return "StepFeaVolumeElementProperty{" + "id=" + id + "name=" + name + "properties=" + properties + "material=" + material + "}";
    }
}
