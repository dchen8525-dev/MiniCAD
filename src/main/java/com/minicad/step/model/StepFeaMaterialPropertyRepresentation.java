package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FEA_MATERIAL_PROPERTY_REPRESENTATION.
 * Material properties for finite element analysis.
 */
/**
 * Resolved FEA_MATERIAL_PROPERTY_REPRESENTATION.
 * Material properties for finite element analysis.
 */
public final class StepFeaMaterialPropertyRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity material;
    private final List<StepEntity> properties;

    public StepFeaMaterialPropertyRepresentation(int id, String name, StepEntity material, List<StepEntity> properties) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.properties = properties == null ? null : java.util.List.copyOf(properties);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getMaterial() {
        return material;
    }

    public List<StepEntity> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaMaterialPropertyRepresentation that = (StepFeaMaterialPropertyRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(material, that.material) && Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, material, properties);
    }

    @Override
    public String toString() {
        return "StepFeaMaterialPropertyRepresentation{" + "id=" + id + "name=" + name + "material=" + material + "properties=" + properties + "}";
    }
}
