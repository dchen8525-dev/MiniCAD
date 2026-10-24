package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "area", area, "material", material);
    }
}
