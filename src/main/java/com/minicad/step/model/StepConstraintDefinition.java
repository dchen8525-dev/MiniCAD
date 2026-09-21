package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepConstraintDefinition extends AbstractStepEntity {
    private final String constraintType;
    private final String constraintExpression;
    private final List<String> constraintParameters;
    private final int constraintSeverity;
    private final String constraintStatus;

    public StepConstraintDefinition(int id, String name, String constraintType, String constraintExpression, List<String> constraintParameters, int constraintSeverity, String constraintStatus) {
        super(id, name);
        this.constraintType = constraintType;
        this.constraintExpression = constraintExpression;
        this.constraintParameters = constraintParameters == null ? null : java.util.List.copyOf(constraintParameters);
        this.constraintSeverity = constraintSeverity;
        this.constraintStatus = constraintStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("constraintType", constraintType);
        state.put("constraintExpression", constraintExpression);
        state.put("constraintParameters", constraintParameters);
        state.put("constraintSeverity", constraintSeverity);
        state.put("constraintStatus", constraintStatus);
        return state;
    }
}
