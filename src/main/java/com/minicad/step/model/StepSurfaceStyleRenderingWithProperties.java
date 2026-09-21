package com.minicad.step.model;

import com.minicad.step.model.StepEntity;import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public final class StepSurfaceStyleRenderingWithProperties extends AbstractStepEntity {
    private final List<StepEntity> properties;

    public StepSurfaceStyleRenderingWithProperties(int id, String name, List<StepEntity> properties) {
        super(id, name);
        this.properties = properties == null ? null : java.util.List.copyOf(properties);
    }

    public List<StepEntity> getProperties() {
        return properties;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("properties", properties);
        return state;
    }
}
