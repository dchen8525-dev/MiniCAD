package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved PART_DEFINITION.
 * A part definition entity.
 *
 * @param id STEP instance id
 * @param name part name
 * @param partId part identifier
 * @param partType part type classification
 * @param geometryDefinition geometry definition reference
 * @param material material reference
 */
/**
 * Resolved PART_DEFINITION.
 * A part definition entity.
 *
 * @param id STEP instance id
 * @param name part name
 * @param partId part identifier
 * @param partType part type classification
 * @param geometryDefinition geometry definition reference
 * @param material material reference
 */
public final class StepPartDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String partId;
    private final String partType;
    private final StepEntity geometryDefinition;
    private final StepEntity material;

    public StepPartDefinition(int id, String name, String partId, String partType, StepEntity geometryDefinition, StepEntity material) {
        this.id = id;
        this.name = name;
        this.partId = partId;
        this.partType = partType;
        this.geometryDefinition = geometryDefinition;
        this.material = material;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPartId() {
        return partId;
    }

    public String getPartType() {
        return partType;
    }

    public StepEntity getGeometryDefinition() {
        return geometryDefinition;
    }

    public StepEntity getMaterial() {
        return material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPartDefinition that = (StepPartDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(partId, that.partId) && Objects.equals(partType, that.partType) && Objects.equals(geometryDefinition, that.geometryDefinition) && Objects.equals(material, that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, partId, partType, geometryDefinition, material);
    }

    @Override
    public String toString() {
        return "StepPartDefinition{" + "id=" + id + "name=" + name + "partId=" + partId + "partType=" + partType + "geometryDefinition=" + geometryDefinition + "material=" + material + "}";
    }
}