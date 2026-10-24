package com.minicad.step.model;

import java.util.List;
import java.util.Map;

public final class StepFeaVolumeElementProperty extends AbstractStepEntity {
    private final List<StepEntity> properties;
    private final StepEntity material;

    public StepFeaVolumeElementProperty(int id, String name, List<StepEntity> properties, StepEntity material) {
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
        return componentsOf(
                "id", getId(), "name", getName(), "properties", properties, "material", material);
    }
}
