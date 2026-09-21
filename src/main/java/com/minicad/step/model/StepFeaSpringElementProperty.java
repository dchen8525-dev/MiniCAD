package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("springConstant", springConstant);
        state.put("material", material);
        return state;
    }
}
