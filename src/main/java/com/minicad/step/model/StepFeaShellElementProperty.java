package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FEA_SHELL_ELEMENT_PROPERTY.
 */
public final class StepFeaShellElementProperty extends AbstractStepEntity {
    private final List<StepEntity> properties;
    private final StepEntity material;

    public StepFeaShellElementProperty(int id, String name, List<StepEntity> properties, StepEntity material) {
        super(id, name);
        this.properties = properties == null ? null : java.util.List.copyOf(properties);
        this.material = material;
    }

    public List<StepEntity> getProperties() {
        return properties;
    }

    public StepEntity getMaterial() {
        return material;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("properties", properties);
        state.put("material", material);
        return state;
    }
}
