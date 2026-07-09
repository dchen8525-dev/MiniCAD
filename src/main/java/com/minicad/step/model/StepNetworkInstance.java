package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved NETWORK_INSTANCE.
 * A network instance entity.
 *
 * @param id STEP instance id
 * @param name network instance name
 * @param networkDefinition network variance definition reference
 * @param networkState network variance state
 * @param networkTraffic network variance traffic level
 * @param networkBandwidth network variance bandwidth
 * @param networkStatus network variance status
 */
/**
 * Resolved NETWORK_INSTANCE.
 * A network instance entity.
 *
 * @param id STEP instance id
 * @param name network instance name
 * @param networkDefinition network variance definition reference
 * @param networkState network variance state
 * @param networkTraffic network variance traffic level
 * @param networkBandwidth network variance bandwidth
 * @param networkStatus network variance status
 */
public final class StepNetworkInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity networkDefinition;
    private final String networkState;
    private final double networkTraffic;
    private final double networkBandwidth;
    private final String networkStatus;

    public StepNetworkInstance(int id, String name, StepEntity networkDefinition, String networkState, double networkTraffic, double networkBandwidth, String networkStatus) {
        this.id = id;
        this.name = name;
        this.networkDefinition = networkDefinition;
        this.networkState = networkState;
        this.networkTraffic = networkTraffic;
        this.networkBandwidth = networkBandwidth;
        this.networkStatus = networkStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getNetworkDefinition() {
        return networkDefinition;
    }

    public String getNetworkState() {
        return networkState;
    }

    public double getNetworkTraffic() {
        return networkTraffic;
    }

    public double getNetworkBandwidth() {
        return networkBandwidth;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNetworkInstance that = (StepNetworkInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(networkDefinition, that.networkDefinition) && Objects.equals(networkState, that.networkState) && networkTraffic == that.networkTraffic && networkBandwidth == that.networkBandwidth && Objects.equals(networkStatus, that.networkStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, networkDefinition, networkState, networkTraffic, networkBandwidth, networkStatus);
    }

    @Override
    public String toString() {
        return "StepNetworkInstance{" + "id=" + id + "name=" + name + "networkDefinition=" + networkDefinition + "networkState=" + networkState + "networkTraffic=" + networkTraffic + "networkBandwidth=" + networkBandwidth + "networkStatus=" + networkStatus + "}";
    }
}