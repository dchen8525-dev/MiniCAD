package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved FEA_SPRING_ELEMENT_PROPERTY.
 */
public final class StepFeaSpringElementProperty extends AbstractStepEntity {
    private final double springConstant;
    private final StepEntity material;

    public StepFeaSpringElementProperty(int id, String name, double springConstant, StepEntity material) {
        super(id, name);
        this.springConstant = springConstant;
        this.material = material;
    }

    public double getSpringConstant() {
        return springConstant;
    }

    public StepEntity getMaterial() {
        return material;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "springConstant", springConstant, "material", material);
    }
}
