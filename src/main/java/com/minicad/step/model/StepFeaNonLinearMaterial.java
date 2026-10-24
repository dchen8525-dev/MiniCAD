package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved FEA_NON_LINEAR_MATERIAL.
 * A non-linear material definition for FEA.
 */
public final class StepFeaNonLinearMaterial extends AbstractStepEntity {
    private final StepEntity material;
    private final String nonLinearModel;

    public StepFeaNonLinearMaterial(int id, String name, StepEntity material, String nonLinearModel) {
        super(id, name);
        this.material = material;
        this.nonLinearModel = nonLinearModel;
    }

    public StepEntity getMaterial() {
        return material;
    }

    public String getNonLinearModel() {
        return nonLinearModel;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "material", material, "nonLinearModel", nonLinearModel);
    }
}
