package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepNetworkDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String networkType;
    private final String networkTopology;
    private final List<StepEntity> networkNodes;
    private final List<StepEntity> networkLinks;
    private final String networkStatus;

    public StepNetworkDefinition(int id, String name, String networkType, String networkTopology, List<StepEntity> networkNodes, List<StepEntity> networkLinks, String networkStatus) {
        this.id = id;
        this.name = name;
        this.networkType = networkType;
        this.networkTopology = networkTopology;
        this.networkNodes = networkNodes == null ? null : java.util.List.copyOf(networkNodes);
        this.networkLinks = networkLinks == null ? null : java.util.List.copyOf(networkLinks);
        this.networkStatus = networkStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNetworkDefinition that = (StepNetworkDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(networkType, that.networkType) && Objects.equals(networkTopology, that.networkTopology) && Objects.equals(networkNodes, that.networkNodes) && Objects.equals(networkLinks, that.networkLinks) && Objects.equals(networkStatus, that.networkStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, networkType, networkTopology, networkNodes, networkLinks, networkStatus);
    }

    @Override
    public String toString() {
        return "StepNetworkDefinition{" + "id=" + id + "name=" + name + "networkType=" + networkType + "networkTopology=" + networkTopology + "networkNodes=" + networkNodes + "networkLinks=" + networkLinks + "networkStatus=" + networkStatus + "}";
    }
}