package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepPartDefinition extends AbstractStepEntity {
    private final String partId;
    private final String partType;
    private final StepEntity geometryDefinition;
    private final StepEntity material;

    public StepPartDefinition(int id, String name, String partId, String partType, StepEntity geometryDefinition, StepEntity material) {
        super(id, name);
        this.partId = partId;
        this.partType = partType;
        this.geometryDefinition = geometryDefinition;
        this.material = material;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("partId", partId);
        state.put("partType", partType);
        state.put("geometryDefinition", geometryDefinition);
        state.put("material", material);
        return state;
    }
}
