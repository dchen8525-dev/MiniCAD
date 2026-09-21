package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FEA_TRUSS_ELEMENT_PROPERTY.
 */
public final class StepFeaTrussElementProperty extends AbstractStepEntity {
    private final double area;
    private final StepEntity material;

    public StepFeaTrussElementProperty(int id, String name, double area, StepEntity material) {
        super(id, name);
        this.area = area;
        this.material = material;
    }

    public double getArea() {
        return area;
    }

    public StepEntity getMaterial() {
        return material;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("area", area);
        state.put("material", material);
        return state;
    }
}
