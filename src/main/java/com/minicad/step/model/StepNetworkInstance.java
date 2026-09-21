package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepNetworkInstance extends AbstractStepEntity {
    private final StepEntity networkDefinition;
    private final String networkState;
    private final double networkTraffic;
    private final double networkBandwidth;
    private final String networkStatus;

    public StepNetworkInstance(int id, String name, StepEntity networkDefinition, String networkState, double networkTraffic, double networkBandwidth, String networkStatus) {
        super(id, name);
        this.networkDefinition = networkDefinition;
        this.networkState = networkState;
        this.networkTraffic = networkTraffic;
        this.networkBandwidth = networkBandwidth;
        this.networkStatus = networkStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("networkDefinition", networkDefinition);
        state.put("networkState", networkState);
        state.put("networkTraffic", networkTraffic);
        state.put("networkBandwidth", networkBandwidth);
        state.put("networkStatus", networkStatus);
        return state;
    }
}
