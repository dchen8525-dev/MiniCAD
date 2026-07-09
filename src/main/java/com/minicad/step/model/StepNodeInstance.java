package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepNodeInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity nodeDefinition;
    private final String nodeState;
    private final String nodeAddress;
    private final double nodeLoad;
    private final String nodeStatus;

    public StepNodeInstance(int id, String name, StepEntity nodeDefinition, String nodeState, String nodeAddress, double nodeLoad, String nodeStatus) {
        this.id = id;
        this.name = name;
        this.nodeDefinition = nodeDefinition;
        this.nodeState = nodeState;
        this.nodeAddress = nodeAddress;
        this.nodeLoad = nodeLoad;
        this.nodeStatus = nodeStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNodeInstance that = (StepNodeInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(nodeDefinition, that.nodeDefinition) && Objects.equals(nodeState, that.nodeState) && Objects.equals(nodeAddress, that.nodeAddress) && nodeLoad == that.nodeLoad && Objects.equals(nodeStatus, that.nodeStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nodeDefinition, nodeState, nodeAddress, nodeLoad, nodeStatus);
    }

    @Override
    public String toString() {
        return "StepNodeInstance{" + "id=" + id + "name=" + name + "nodeDefinition=" + nodeDefinition + "nodeState=" + nodeState + "nodeAddress=" + nodeAddress + "nodeLoad=" + nodeLoad + "nodeStatus=" + nodeStatus + "}";
    }
}