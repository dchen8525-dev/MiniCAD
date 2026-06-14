package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONSTRAINT_DEFINITION.
 * A constraint definition entity.
 *
 * @param id STEP instance id
 * @param name constraint name
 * @param constraintType constraint variance type
 * @param constraintExpression constraint variance expression
 * @param constraintParameters constraint variance parameters
 * @param constraintSeverity constraint variance severity level
 * @param constraintStatus constraint variance status
 */
/**
 * Resolved CONSTRAINT_DEFINITION.
 * A constraint definition entity.
 *
 * @param id STEP instance id
 * @param name constraint name
 * @param constraintType constraint variance type
 * @param constraintExpression constraint variance expression
 * @param constraintParameters constraint variance parameters
 * @param constraintSeverity constraint variance severity level
 * @param constraintStatus constraint variance status
 */
public final class StepConstraintDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String constraintType;
    private final String constraintExpression;
    private final List<String> constraintParameters;
    private final int constraintSeverity;
    private final String constraintStatus;

    public StepConstraintDefinition(int id, String name, String constraintType, String constraintExpression, List<String> constraintParameters, int constraintSeverity, String constraintStatus) {
        this.id = id;
        this.name = name;
        this.constraintType = constraintType;
        this.constraintExpression = constraintExpression;
        this.constraintParameters = constraintParameters == null ? null : java.util.List.copyOf(constraintParameters);
        this.constraintSeverity = constraintSeverity;
        this.constraintStatus = constraintStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public String getConstraintExpression() {
        return constraintExpression;
    }

    public List<String> getConstraintParameters() {
        return constraintParameters;
    }

    public int getConstraintSeverity() {
        return constraintSeverity;
    }

    public String getConstraintStatus() {
        return constraintStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConstraintDefinition that = (StepConstraintDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(constraintType, that.constraintType) && Objects.equals(constraintExpression, that.constraintExpression) && Objects.equals(constraintParameters, that.constraintParameters) && constraintSeverity == that.constraintSeverity && Objects.equals(constraintStatus, that.constraintStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, constraintType, constraintExpression, constraintParameters, constraintSeverity, constraintStatus);
    }

    @Override
    public String toString() {
        return "StepConstraintDefinition{" + "id=" + id + "name=" + name + "constraintType=" + constraintType + "constraintExpression=" + constraintExpression + "constraintParameters=" + constraintParameters + "constraintSeverity=" + constraintSeverity + "constraintStatus=" + constraintStatus + "}";
    }
}