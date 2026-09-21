package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CONNECTION_INSTANCE.
 * A connection instance entity.
 *
 * @param id STEP instance id
 * @param name connection instance name
 * @param connectionDefinition connection variance definition reference
 * @param connectionState connection variance state
 * @param connectionLatency connection variance latency
 * @param connectionThroughput connection variance throughput
 * @param connectionStatus connection variance status
 */
public final class StepConnectionInstance extends AbstractStepEntity {
    private final StepEntity connectionDefinition;
    private final String connectionState;
    private final double connectionLatency;
    private final double connectionThroughput;
    private final String connectionStatus;

    public StepConnectionInstance(int id, String name, StepEntity connectionDefinition, String connectionState, double connectionLatency, double connectionThroughput, String connectionStatus) {
        super(id, name);
        this.connectionDefinition = connectionDefinition;
        this.connectionState = connectionState;
        this.connectionLatency = connectionLatency;
        this.connectionThroughput = connectionThroughput;
        this.connectionStatus = connectionStatus;
    }

    public StepEntity getConnectionDefinition() {
        return connectionDefinition;
    }

    public String getConnectionState() {
        return connectionState;
    }

    public double getConnectionLatency() {
        return connectionLatency;
    }

    public double getConnectionThroughput() {
        return connectionThroughput;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("connectionDefinition", connectionDefinition);
        state.put("connectionState", connectionState);
        state.put("connectionLatency", connectionLatency);
        state.put("connectionThroughput", connectionThroughput);
        state.put("connectionStatus", connectionStatus);
        return state;
    }
}
