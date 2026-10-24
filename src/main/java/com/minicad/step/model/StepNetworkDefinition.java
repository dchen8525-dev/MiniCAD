package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved NETWORK_DEFINITION.
 * A network definition entity.
 *
 * @param id STEP instance id
 * @param name network name
 * @param networkType network variance type
 * @param networkTopology network variance topology
 * @param networkNodes network variance node definitions
 * @param networkLinks network variance link definitions
 * @param networkStatus network variance status
 */
public final class StepNetworkDefinition extends AbstractStepEntity {
    private final String networkType;
    private final String networkTopology;
    private final List<StepEntity> networkNodes;
    private final List<StepEntity> networkLinks;
    private final String networkStatus;

    public StepNetworkDefinition(int id, String name, String networkType, String networkTopology, List<StepEntity> networkNodes, List<StepEntity> networkLinks, String networkStatus) {
        super(id, name);
        this.networkType = networkType;
        this.networkTopology = networkTopology;
        this.networkNodes = networkNodes == null ? null : java.util.List.copyOf(networkNodes);
        this.networkLinks = networkLinks == null ? null : java.util.List.copyOf(networkLinks);
        this.networkStatus = networkStatus;
    }

    public String getNetworkType() {
        return networkType;
    }

    public String getNetworkTopology() {
        return networkTopology;
    }

    public List<StepEntity> getNetworkNodes() {
        return networkNodes;
    }

    public List<StepEntity> getNetworkLinks() {
        return networkLinks;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "networkType", networkType,
                "networkTopology", networkTopology, "networkNodes", networkNodes,
                "networkLinks", networkLinks, "networkStatus", networkStatus);
    }
}
