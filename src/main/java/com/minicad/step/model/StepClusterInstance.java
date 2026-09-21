package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepClusterInstance extends AbstractStepEntity {
    private final StepEntity clusterDefinition;
    private final String clusterState;
    private final int clusterNodeCount;
    private final double clusterLoad;
    private final String clusterStatus;

    public StepClusterInstance(int id, String name, StepEntity clusterDefinition, String clusterState, int clusterNodeCount, double clusterLoad, String clusterStatus) {
        super(id, name);
        this.clusterDefinition = clusterDefinition;
        this.clusterState = clusterState;
        this.clusterNodeCount = clusterNodeCount;
        this.clusterLoad = clusterLoad;
        this.clusterStatus = clusterStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("clusterDefinition", clusterDefinition);
        state.put("clusterState", clusterState);
        state.put("clusterNodeCount", clusterNodeCount);
        state.put("clusterLoad", clusterLoad);
        state.put("clusterStatus", clusterStatus);
        return state;
    }
}
