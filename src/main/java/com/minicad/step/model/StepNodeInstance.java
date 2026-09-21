package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved NODE_INSTANCE.
 * A node instance entity.
 *
 * @param id STEP instance id
 * @param name node instance name
 * @param nodeDefinition node variance definition reference
 * @param nodeState node variance state
 * @param nodeAddress node variance address/identifier
 * @param nodeLoad node variance load percentage
 * @param nodeStatus node variance status
 */
public final class StepNodeInstance extends AbstractStepEntity {
    private final StepEntity nodeDefinition;
    private final String nodeState;
    private final String nodeAddress;
    private final double nodeLoad;
    private final String nodeStatus;

    public StepNodeInstance(int id, String name, StepEntity nodeDefinition, String nodeState, String nodeAddress, double nodeLoad, String nodeStatus) {
        super(id, name);
        this.nodeDefinition = nodeDefinition;
        this.nodeState = nodeState;
        this.nodeAddress = nodeAddress;
        this.nodeLoad = nodeLoad;
        this.nodeStatus = nodeStatus;
    }

    public StepEntity getNodeDefinition() {
        return nodeDefinition;
    }

    public String getNodeState() {
        return nodeState;
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    public double getNodeLoad() {
        return nodeLoad;
    }

    public String getNodeStatus() {
        return nodeStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("nodeDefinition", nodeDefinition);
        state.put("nodeState", nodeState);
        state.put("nodeAddress", nodeAddress);
        state.put("nodeLoad", nodeLoad);
        state.put("nodeStatus", nodeStatus);
        return state;
    }
}
