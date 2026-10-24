package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved FEA_BEAM_ELEMENT_PROPERTY.
 */
public final class StepFeaBeamElementProperty extends AbstractStepEntity {
    private final List<StepEntity> properties;
    private final StepEntity material;
    private final StepEntity crossSection;

    public StepFeaBeamElementProperty(int id, String name, List<StepEntity> properties, StepEntity material, StepEntity crossSection) {
        super(id, name);
        this.properties = properties == null ? null : java.util.List.copyOf(properties);
        this.material = material;
        this.crossSection = crossSection;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "properties", properties, "material", material,
                "crossSection", crossSection);
    }
}
