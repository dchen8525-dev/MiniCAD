package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CONSTRAINT_INSTANCE.
 * A constraint instance entity.
 *
 * @param id STEP instance id
 * @param name constraint instance name
 * @param constraintDefinition constraint variance definition reference
 * @param constraintState constraint variance state
 * @param constraintValue constraint variance current value
 * @param constraintViolations constraint variance violation count
 * @param constraintStatus constraint variance status
 */
public final class StepConstraintInstance extends AbstractStepEntity {
    private final StepEntity constraintDefinition;
    private final String constraintState;
    private final String constraintValue;
    private final int constraintViolations;
    private final String constraintStatus;

    public StepConstraintInstance(int id, String name, StepEntity constraintDefinition, String constraintState, String constraintValue, int constraintViolations, String constraintStatus) {
        super(id, name);
        this.constraintDefinition = constraintDefinition;
        this.constraintState = constraintState;
        this.constraintValue = constraintValue;
        this.constraintViolations = constraintViolations;
        this.constraintStatus = constraintStatus;
    }

    public StepEntity getConstraintDefinition() {
        return constraintDefinition;
    }

    public String getConstraintState() {
        return constraintState;
    }

    public String getConstraintValue() {
        return constraintValue;
    }

    public int getConstraintViolations() {
        return constraintViolations;
    }

    public String getConstraintStatus() {
        return constraintStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("constraintDefinition", constraintDefinition);
        state.put("constraintState", constraintState);
        state.put("constraintValue", constraintValue);
        state.put("constraintViolations", constraintViolations);
        state.put("constraintStatus", constraintStatus);
        return state;
    }
}
