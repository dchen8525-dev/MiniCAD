package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepConnectionInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity connectionDefinition;
    private final String connectionState;
    private final double connectionLatency;
    private final double connectionThroughput;
    private final String connectionStatus;

    public StepConnectionInstance(int id, String name, StepEntity connectionDefinition, String connectionState, double connectionLatency, double connectionThroughput, String connectionStatus) {
        this.id = id;
        this.name = name;
        this.connectionDefinition = connectionDefinition;
        this.connectionState = connectionState;
        this.connectionLatency = connectionLatency;
        this.connectionThroughput = connectionThroughput;
        this.connectionStatus = connectionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConnectionInstance that = (StepConnectionInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(connectionDefinition, that.connectionDefinition) && Objects.equals(connectionState, that.connectionState) && connectionLatency == that.connectionLatency && connectionThroughput == that.connectionThroughput && Objects.equals(connectionStatus, that.connectionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, connectionDefinition, connectionState, connectionLatency, connectionThroughput, connectionStatus);
    }

    @Override
    public String toString() {
        return "StepConnectionInstance{" + "id=" + id + "name=" + name + "connectionDefinition=" + connectionDefinition + "connectionState=" + connectionState + "connectionLatency=" + connectionLatency + "connectionThroughput=" + connectionThroughput + "connectionStatus=" + connectionStatus + "}";
    }
}