package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved NODE_DEFINITION.
 * A node definition entity.
 *
 * @param id STEP instance id
 * @param name node name
 * @param nodeType node variance type
 * @param nodeLocation node variance location reference
 * @param nodeCapabilities node variance capabilities
 * @param nodeConnections node variance connections
 * @param nodeStatus node variance status
 */
public final class StepNodeDefinition extends AbstractStepEntity {
    private final String nodeType;
    private final StepEntity nodeLocation;
    private final List<String> nodeCapabilities;
    private final List<StepEntity> nodeConnections;
    private final String nodeStatus;

    public StepNodeDefinition(int id, String name, String nodeType, StepEntity nodeLocation, List<String> nodeCapabilities, List<StepEntity> nodeConnections, String nodeStatus) {
        super(id, name);
        this.nodeType = nodeType;
        this.nodeLocation = nodeLocation;
        this.nodeCapabilities = nodeCapabilities == null ? null : java.util.List.copyOf(nodeCapabilities);
        this.nodeConnections = nodeConnections == null ? null : java.util.List.copyOf(nodeConnections);
        this.nodeStatus = nodeStatus;
    }

    public String getNodeType() {
        return nodeType;
    }

    public StepEntity getNodeLocation() {
        return nodeLocation;
    }

    public List<String> getNodeCapabilities() {
        return nodeCapabilities;
    }

    public List<StepEntity> getNodeConnections() {
        return nodeConnections;
    }

    public String getNodeStatus() {
        return nodeStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("nodeType", nodeType);
        state.put("nodeLocation", nodeLocation);
        state.put("nodeCapabilities", nodeCapabilities);
        state.put("nodeConnections", nodeConnections);
        state.put("nodeStatus", nodeStatus);
        return state;
    }
}
