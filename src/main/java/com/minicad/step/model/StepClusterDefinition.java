package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepClusterDefinition extends AbstractStepEntity {
    private final String clusterType;
    private final List<StepEntity> clusterNodes;
    private final String clusterPolicy;
    private final double clusterCapacity;
    private final String clusterStatus;

    public StepClusterDefinition(int id, String name, String clusterType, List<StepEntity> clusterNodes, String clusterPolicy, double clusterCapacity, String clusterStatus) {
        super(id, name);
        this.clusterType = clusterType;
        this.clusterNodes = clusterNodes == null ? null : java.util.List.copyOf(clusterNodes);
        this.clusterPolicy = clusterPolicy;
        this.clusterCapacity = clusterCapacity;
        this.clusterStatus = clusterStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("clusterType", clusterType);
        state.put("clusterNodes", clusterNodes);
        state.put("clusterPolicy", clusterPolicy);
        state.put("clusterCapacity", clusterCapacity);
        state.put("clusterStatus", clusterStatus);
        return state;
    }
}
