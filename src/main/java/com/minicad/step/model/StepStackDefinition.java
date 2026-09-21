package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STACK_DEFINITION.
 * A stack definition entity.
 *
 * @param id STEP instance id
 * @param name stack name
 * @param stackType stack variance type
 * @param stackCapacity stack variance capacity
 * @param stackPolicy stack variance policy
 * @param stackStatus stack variance status
 */
public final class StepStackDefinition extends AbstractStepEntity {
    private final String stackType;
    private final int stackCapacity;
    private final String stackPolicy;
    private final String stackStatus;

    public StepStackDefinition(int id, String name, String stackType, int stackCapacity, String stackPolicy, String stackStatus) {
        super(id, name);
        this.stackType = stackType;
        this.stackCapacity = stackCapacity;
        this.stackPolicy = stackPolicy;
        this.stackStatus = stackStatus;
    }

    public String getStackType() {
        return stackType;
    }

    public int getStackCapacity() {
        return stackCapacity;
    }

    public String getStackPolicy() {
        return stackPolicy;
    }

    public String getStackStatus() {
        return stackStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("stackType", stackType);
        state.put("stackCapacity", stackCapacity);
        state.put("stackPolicy", stackPolicy);
        state.put("stackStatus", stackStatus);
        return state;
    }
}
