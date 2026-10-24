package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "variable", variable, "constraint", constraint);
    }
}
