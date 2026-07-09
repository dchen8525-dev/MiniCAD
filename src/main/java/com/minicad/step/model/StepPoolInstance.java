package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPoolInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity poolDefinition;
    private final String poolState;
    private final double poolUsed;
    private final double poolAvailable;
    private final String poolStatus;

    public StepPoolInstance(int id, String name, StepEntity poolDefinition, String poolState, double poolUsed, double poolAvailable, String poolStatus) {
        this.id = id;
        this.name = name;
        this.poolDefinition = poolDefinition;
        this.poolState = poolState;
        this.poolUsed = poolUsed;
        this.poolAvailable = poolAvailable;
        this.poolStatus = poolStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPoolInstance that = (StepPoolInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(poolDefinition, that.poolDefinition) && Objects.equals(poolState, that.poolState) && poolUsed == that.poolUsed && poolAvailable == that.poolAvailable && Objects.equals(poolStatus, that.poolStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, poolDefinition, poolState, poolUsed, poolAvailable, poolStatus);
    }

    @Override
    public String toString() {
        return "StepPoolInstance{" + "id=" + id + "name=" + name + "poolDefinition=" + poolDefinition + "poolState=" + poolState + "poolUsed=" + poolUsed + "poolAvailable=" + poolAvailable + "poolStatus=" + poolStatus + "}";
    }
}