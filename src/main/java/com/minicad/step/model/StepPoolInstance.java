package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved POOL_INSTANCE.
 * A pool instance entity.
 *
 * @param id STEP instance id
 * @param name pool instance name
 * @param poolDefinition pool variance definition reference
 * @param poolState pool variance state
 * @param poolUsed pool variance used capacity
 * @param poolAvailable pool variance available capacity
 * @param poolStatus pool variance status
 */
public final class StepPoolInstance extends AbstractStepEntity {
    private final StepEntity poolDefinition;
    private final String poolState;
    private final double poolUsed;
    private final double poolAvailable;
    private final String poolStatus;

    public StepPoolInstance(int id, String name, StepEntity poolDefinition, String poolState, double poolUsed, double poolAvailable, String poolStatus) {
        super(id, name);
        this.poolDefinition = poolDefinition;
        this.poolState = poolState;
        this.poolUsed = poolUsed;
        this.poolAvailable = poolAvailable;
        this.poolStatus = poolStatus;
    }

    public StepEntity getPoolDefinition() {
        return poolDefinition;
    }

    public String getPoolState() {
        return poolState;
    }

    public double getPoolUsed() {
        return poolUsed;
    }

    public double getPoolAvailable() {
        return poolAvailable;
    }

    public String getPoolStatus() {
        return poolStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("poolDefinition", poolDefinition);
        state.put("poolState", poolState);
        state.put("poolUsed", poolUsed);
        state.put("poolAvailable", poolAvailable);
        state.put("poolStatus", poolStatus);
        return state;
    }
}
