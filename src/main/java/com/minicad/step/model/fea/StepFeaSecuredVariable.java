package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved FEA_SECURED_VARIABLE.
 * A secured (constrained) variable in finite element analysis.
 */
/**
 * Resolved FEA_SECURED_VARIABLE.
 * A secured (constrained) variable in finite element analysis.
 */
public final class StepFeaSecuredVariable implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity variable;
    private final StepEntity constraint;

    public StepFeaSecuredVariable(int id, String name, StepEntity variable, StepEntity constraint) {
        this.id = id;
        this.name = name;
        this.variable = variable;
        this.constraint = constraint;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVariable() {
        return variable;
    }

    public StepEntity getConstraint() {
        return constraint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaSecuredVariable that = (StepFeaSecuredVariable) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(variable, that.variable) && Objects.equals(constraint, that.constraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, variable, constraint);
    }

    @Override
    public String toString() {
        return "StepFeaSecuredVariable{" + "id=" + id + "name=" + name + "variable=" + variable + "constraint=" + constraint + "}";
    }
}
