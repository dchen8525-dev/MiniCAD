package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepNodeDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String nodeType;
    private final StepEntity nodeLocation;
    private final List<String> nodeCapabilities;
    private final List<StepEntity> nodeConnections;
    private final String nodeStatus;

    public StepNodeDefinition(int id, String name, String nodeType, StepEntity nodeLocation, List<String> nodeCapabilities, List<StepEntity> nodeConnections, String nodeStatus) {
        this.id = id;
        this.name = name;
        this.nodeType = nodeType;
        this.nodeLocation = nodeLocation;
        this.nodeCapabilities = nodeCapabilities == null ? null : java.util.List.copyOf(nodeCapabilities);
        this.nodeConnections = nodeConnections == null ? null : java.util.List.copyOf(nodeConnections);
        this.nodeStatus = nodeStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNodeDefinition that = (StepNodeDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(nodeType, that.nodeType) && Objects.equals(nodeLocation, that.nodeLocation) && Objects.equals(nodeCapabilities, that.nodeCapabilities) && Objects.equals(nodeConnections, that.nodeConnections) && Objects.equals(nodeStatus, that.nodeStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nodeType, nodeLocation, nodeCapabilities, nodeConnections, nodeStatus);
    }

    @Override
    public String toString() {
        return "StepNodeDefinition{" + "id=" + id + "name=" + name + "nodeType=" + nodeType + "nodeLocation=" + nodeLocation + "nodeCapabilities=" + nodeCapabilities + "nodeConnections=" + nodeConnections + "nodeStatus=" + nodeStatus + "}";
    }
}