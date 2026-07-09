package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CLUSTER_DEFINITION.
 * A cluster definition entity.
 *
 * @param id STEP instance id
 * @param name cluster name
 * @param clusterType cluster variance type
 * @param clusterNodes cluster variance node definitions
 * @param clusterPolicy cluster variance policy
 * @param clusterCapacity cluster variance capacity
 * @param clusterStatus cluster variance status
 */
/**
 * Resolved CLUSTER_DEFINITION.
 * A cluster definition entity.
 *
 * @param id STEP instance id
 * @param name cluster name
 * @param clusterType cluster variance type
 * @param clusterNodes cluster variance node definitions
 * @param clusterPolicy cluster variance policy
 * @param clusterCapacity cluster variance capacity
 * @param clusterStatus cluster variance status
 */
public final class StepClusterDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String clusterType;
    private final List<StepEntity> clusterNodes;
    private final String clusterPolicy;
    private final double clusterCapacity;
    private final String clusterStatus;

    public StepClusterDefinition(int id, String name, String clusterType, List<StepEntity> clusterNodes, String clusterPolicy, double clusterCapacity, String clusterStatus) {
        this.id = id;
        this.name = name;
        this.clusterType = clusterType;
        this.clusterNodes = clusterNodes == null ? null : java.util.List.copyOf(clusterNodes);
        this.clusterPolicy = clusterPolicy;
        this.clusterCapacity = clusterCapacity;
        this.clusterStatus = clusterStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getClusterType() {
        return clusterType;
    }

    public List<StepEntity> getClusterNodes() {
        return clusterNodes;
    }

    public String getClusterPolicy() {
        return clusterPolicy;
    }

    public double getClusterCapacity() {
        return clusterCapacity;
    }

    public String getClusterStatus() {
        return clusterStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepClusterDefinition that = (StepClusterDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(clusterType, that.clusterType) && Objects.equals(clusterNodes, that.clusterNodes) && Objects.equals(clusterPolicy, that.clusterPolicy) && clusterCapacity == that.clusterCapacity && Objects.equals(clusterStatus, that.clusterStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, clusterType, clusterNodes, clusterPolicy, clusterCapacity, clusterStatus);
    }

    @Override
    public String toString() {
        return "StepClusterDefinition{" + "id=" + id + "name=" + name + "clusterType=" + clusterType + "clusterNodes=" + clusterNodes + "clusterPolicy=" + clusterPolicy + "clusterCapacity=" + clusterCapacity + "clusterStatus=" + clusterStatus + "}";
    }
}