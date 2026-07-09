package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPoolDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String poolType;
    private final List<StepEntity> poolResources;
    private final String poolAllocation;
    private final double poolCapacity;
    private final String poolStatus;

    public StepPoolDefinition(int id, String name, String poolType, List<StepEntity> poolResources, String poolAllocation, double poolCapacity, String poolStatus) {
        this.id = id;
        this.name = name;
        this.poolType = poolType;
        this.poolResources = poolResources == null ? null : java.util.List.copyOf(poolResources);
        this.poolAllocation = poolAllocation;
        this.poolCapacity = poolCapacity;
        this.poolStatus = poolStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPoolDefinition that = (StepPoolDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(poolType, that.poolType) && Objects.equals(poolResources, that.poolResources) && Objects.equals(poolAllocation, that.poolAllocation) && poolCapacity == that.poolCapacity && Objects.equals(poolStatus, that.poolStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, poolType, poolResources, poolAllocation, poolCapacity, poolStatus);
    }

    @Override
    public String toString() {
        return "StepPoolDefinition{" + "id=" + id + "name=" + name + "poolType=" + poolType + "poolResources=" + poolResources + "poolAllocation=" + poolAllocation + "poolCapacity=" + poolCapacity + "poolStatus=" + poolStatus + "}";
    }
}