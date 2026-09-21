package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved POOL_DEFINITION.
 * A pool definition entity.
 *
 * @param id STEP instance id
 * @param name pool name
 * @param poolType pool variance type
 * @param poolResources pool variance resource definitions
 * @param poolAllocation pool variance allocation policy
 * @param poolCapacity pool variance capacity
 * @param poolStatus pool variance status
 */
public final class StepPoolDefinition extends AbstractStepEntity {
    private final String poolType;
    private final List<StepEntity> poolResources;
    private final String poolAllocation;
    private final double poolCapacity;
    private final String poolStatus;

    public StepPoolDefinition(int id, String name, String poolType, List<StepEntity> poolResources, String poolAllocation, double poolCapacity, String poolStatus) {
        super(id, name);
        this.poolType = poolType;
        this.poolResources = poolResources == null ? null : java.util.List.copyOf(poolResources);
        this.poolAllocation = poolAllocation;
        this.poolCapacity = poolCapacity;
        this.poolStatus = poolStatus;
    }

    public String getPoolType() {
        return poolType;
    }

    public List<StepEntity> getPoolResources() {
        return poolResources;
    }

    public String getPoolAllocation() {
        return poolAllocation;
    }

    public double getPoolCapacity() {
        return poolCapacity;
    }

    public String getPoolStatus() {
        return poolStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("poolType", poolType);
        state.put("poolResources", poolResources);
        state.put("poolAllocation", poolAllocation);
        state.put("poolCapacity", poolCapacity);
        state.put("poolStatus", poolStatus);
        return state;
    }
}
