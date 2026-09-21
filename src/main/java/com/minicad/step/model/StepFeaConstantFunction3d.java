package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FEA_CONSTANT_FUNCTION_3D.
 * A constant scalar or vector function in 3D FEA space.
 */
public final class StepFeaConstantFunction3d extends AbstractStepEntity {
    private final Double value;
    private final StepEntity functionSpace;

    public StepFeaConstantFunction3d(int id, String name, Double value, StepEntity functionSpace) {
        super(id, name);
        this.value = value;
        this.functionSpace = functionSpace;
    }

    public Double getValue() {
        return value;
    }

    public StepEntity getFunctionSpace() {
        return functionSpace;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("value", value);
        state.put("functionSpace", functionSpace);
        return state;
    }
}
