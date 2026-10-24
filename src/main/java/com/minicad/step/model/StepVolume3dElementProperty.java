package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved VOLUME_3D_ELEMENT_PROPERTY.
 * Properties associated with 3D volume elements (material, thickness, etc.).
 */
public final class StepVolume3dElementProperty extends AbstractStepEntity {
    private final StepEntity element;
    private final StepEntity property;
    private final StepEntity material;

    public StepVolume3dElementProperty(int id, String name, StepEntity element, StepEntity property, StepEntity material) {
        super(id, name);
        this.element = element;
        this.property = property;
        this.material = material;
    }

    public StepEntity getElement() {
        return element;
    }

    public StepEntity getProperty() {
        return property;
    }

    public StepEntity getMaterial() {
        return material;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "element", element, "property", property,
                "material", material);
    }
}
