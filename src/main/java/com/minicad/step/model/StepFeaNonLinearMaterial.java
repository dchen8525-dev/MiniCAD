package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("material", material);
        state.put("nonLinearModel", nonLinearModel);
        return state;
    }
}
