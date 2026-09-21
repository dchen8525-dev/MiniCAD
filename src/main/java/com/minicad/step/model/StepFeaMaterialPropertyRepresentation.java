package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FEA_MATERIAL_PROPERTY_REPRESENTATION.
 * Material properties for finite element analysis.
 */
public final class StepFeaMaterialPropertyRepresentation extends AbstractStepEntity {
    private final StepEntity material;
    private final List<StepEntity> properties;

    public StepFeaMaterialPropertyRepresentation(int id, String name, StepEntity material, List<StepEntity> properties) {
        super(id, name);
        this.material = material;
        this.properties = properties == null ? null : java.util.List.copyOf(properties);
    }

    public StepEntity getMaterial() {
        return material;
    }

    public List<StepEntity> getProperties() {
        return properties;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("material", material);
        state.put("properties", properties);
        return state;
    }
}
