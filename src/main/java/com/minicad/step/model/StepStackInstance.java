package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved STACK_INSTANCE.
 * A stack instance entity.
 *
 * @param id STEP instance id
 * @param name stack instance name
 * @param stackDefinition stack variance definition reference
 * @param stackState stack variance state
 * @param stackDepth stack variance current depth
 * @param stackStatus stack variance status
 */
public final class StepStackInstance extends AbstractStepEntity {
    private final StepEntity stackDefinition;
    private final String stackState;
    private final int stackDepth;
    private final String stackStatus;

    public StepStackInstance(int id, String name, StepEntity stackDefinition, String stackState, int stackDepth, String stackStatus) {
        super(id, name);
        this.stackDefinition = stackDefinition;
        this.stackState = stackState;
        this.stackDepth = stackDepth;
        this.stackStatus = stackStatus;
    }

    public StepEntity getStackDefinition() {
        return stackDefinition;
    }

    public String getStackState() {
        return stackState;
    }

    public int getStackDepth() {
        return stackDepth;
    }

    public String getStackStatus() {
        return stackStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("stackDefinition", stackDefinition);
        state.put("stackState", stackState);
        state.put("stackDepth", stackDepth);
        state.put("stackStatus", stackStatus);
        return state;
    }
}
