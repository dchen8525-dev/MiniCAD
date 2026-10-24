package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved CURVE_3D_ELEMENT_PROPERTY.
 * Properties associated with 3D curve/beam elements.
 */
public final class StepCurve3dElementProperty extends AbstractStepEntity {
    private final StepEntity element;
    private final StepEntity property;
    private final StepEntity material;

    public StepCurve3dElementProperty(int id, String name, StepEntity element, StepEntity property, StepEntity material) {
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
