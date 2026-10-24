package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "poolDefinition", poolDefinition, "poolState", poolState,
                "poolUsed", poolUsed, "poolAvailable", poolAvailable, "poolStatus", poolStatus);
    }
}
