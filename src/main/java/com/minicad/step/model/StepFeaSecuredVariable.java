package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FEA_SECURED_VARIABLE.
 * A secured (constrained) variable in finite element analysis.
 */
public final class StepFeaSecuredVariable extends AbstractStepEntity {
    private final StepEntity variable;
    private final StepEntity constraint;

    public StepFeaSecuredVariable(int id, String name, StepEntity variable, StepEntity constraint) {
        super(id, name);
        this.variable = variable;
        this.constraint = constraint;
    }

    public StepEntity getVariable() {
        return variable;
    }

    public StepEntity getConstraint() {
        return constraint;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("variable", variable);
        state.put("constraint", constraint);
        return state;
    }
}
