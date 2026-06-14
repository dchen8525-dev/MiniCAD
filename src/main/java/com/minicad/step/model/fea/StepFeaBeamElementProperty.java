package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FEA_BEAM_ELEMENT_PROPERTY.
 */
/**
 * Resolved FEA_BEAM_ELEMENT_PROPERTY.
 */
public final class StepFeaBeamElementProperty implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> properties;
    private final StepEntity material;
    private final StepEntity crossSection;

    public StepFeaBeamElementProperty(int id, String name, List<StepEntity> properties, StepEntity material, StepEntity crossSection) {
        this.id = id;
        this.name = name;
        this.properties = properties == null ? null : java.util.List.copyOf(properties);
        this.material = material;
        this.crossSection = crossSection;
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

    public StepEntity getCrossSection() {
        return crossSection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaBeamElementProperty that = (StepFeaBeamElementProperty) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(properties, that.properties) && Objects.equals(material, that.material) && Objects.equals(crossSection, that.crossSection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, properties, material, crossSection);
    }

    @Override
    public String toString() {
        return "StepFeaBeamElementProperty{" + "id=" + id + "name=" + name + "properties=" + properties + "material=" + material + "crossSection=" + crossSection + "}";
    }
}
