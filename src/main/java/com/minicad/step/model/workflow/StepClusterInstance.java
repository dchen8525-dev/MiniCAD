package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CLUSTER_INSTANCE.
 * A cluster instance entity.
 *
 * @param id STEP instance id
 * @param name cluster instance name
 * @param clusterDefinition cluster variance definition reference
 * @param clusterState cluster variance state
 * @param clusterNodeCount cluster variance active node count
 * @param clusterLoad cluster variance load level
 * @param clusterStatus cluster variance status
 */
/**
 * Resolved CLUSTER_INSTANCE.
 * A cluster instance entity.
 *
 * @param id STEP instance id
 * @param name cluster instance name
 * @param clusterDefinition cluster variance definition reference
 * @param clusterState cluster variance state
 * @param clusterNodeCount cluster variance active node count
 * @param clusterLoad cluster variance load level
 * @param clusterStatus cluster variance status
 */
public final class StepClusterInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity clusterDefinition;
    private final String clusterState;
    private final int clusterNodeCount;
    private final double clusterLoad;
    private final String clusterStatus;

    public StepClusterInstance(int id, String name, StepEntity clusterDefinition, String clusterState, int clusterNodeCount, double clusterLoad, String clusterStatus) {
        this.id = id;
        this.name = name;
        this.clusterDefinition = clusterDefinition;
        this.clusterState = clusterState;
        this.clusterNodeCount = clusterNodeCount;
        this.clusterLoad = clusterLoad;
        this.clusterStatus = clusterStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getClusterDefinition() {
        return clusterDefinition;
    }

    public String getClusterState() {
        return clusterState;
    }

    public int getClusterNodeCount() {
        return clusterNodeCount;
    }

    public double getClusterLoad() {
        return clusterLoad;
    }

    public String getClusterStatus() {
        return clusterStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepClusterInstance that = (StepClusterInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(clusterDefinition, that.clusterDefinition) && Objects.equals(clusterState, that.clusterState) && clusterNodeCount == that.clusterNodeCount && clusterLoad == that.clusterLoad && Objects.equals(clusterStatus, that.clusterStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, clusterDefinition, clusterState, clusterNodeCount, clusterLoad, clusterStatus);
    }

    @Override
    public String toString() {
        return "StepClusterInstance{" + "id=" + id + "name=" + name + "clusterDefinition=" + clusterDefinition + "clusterState=" + clusterState + "clusterNodeCount=" + clusterNodeCount + "clusterLoad=" + clusterLoad + "clusterStatus=" + clusterStatus + "}";
    }
}