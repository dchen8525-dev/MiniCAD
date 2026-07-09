package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONNECTION_DEFINITION.
 * A connection definition entity.
 *
 * @param id STEP instance id
 * @param name connection name
 * @param connectionType connection variance type
 * @param connectionProtocol connection variance protocol
 * @param connectionParameters connection variance parameters
 * @param connectionQuality connection variance quality requirements
 * @param connectionStatus connection variance status
 */
/**
 * Resolved CONNECTION_DEFINITION.
 * A connection definition entity.
 *
 * @param id STEP instance id
 * @param name connection name
 * @param connectionType connection variance type
 * @param connectionProtocol connection variance protocol
 * @param connectionParameters connection variance parameters
 * @param connectionQuality connection variance quality requirements
 * @param connectionStatus connection variance status
 */
public final class StepConnectionDefinition2 implements StepEntity {
    private final int id;
    private final String name;
    private final String connectionType;
    private final String connectionProtocol;
    private final List<String> connectionParameters;
    private final String connectionQuality;
    private final String connectionStatus;

    public StepConnectionDefinition2(int id, String name, String connectionType, String connectionProtocol, List<String> connectionParameters, String connectionQuality, String connectionStatus) {
        this.id = id;
        this.name = name;
        this.connectionType = connectionType;
        this.connectionProtocol = connectionProtocol;
        this.connectionParameters = connectionParameters == null ? null : java.util.List.copyOf(connectionParameters);
        this.connectionQuality = connectionQuality;
        this.connectionStatus = connectionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public String getConnectionProtocol() {
        return connectionProtocol;
    }

    public List<String> getConnectionParameters() {
        return connectionParameters;
    }

    public String getConnectionQuality() {
        return connectionQuality;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConnectionDefinition2 that = (StepConnectionDefinition2) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(connectionType, that.connectionType) && Objects.equals(connectionProtocol, that.connectionProtocol) && Objects.equals(connectionParameters, that.connectionParameters) && Objects.equals(connectionQuality, that.connectionQuality) && Objects.equals(connectionStatus, that.connectionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, connectionType, connectionProtocol, connectionParameters, connectionQuality, connectionStatus);
    }

    @Override
    public String toString() {
        return "StepConnectionDefinition2{" + "id=" + id + "name=" + name + "connectionType=" + connectionType + "connectionProtocol=" + connectionProtocol + "connectionParameters=" + connectionParameters + "connectionQuality=" + connectionQuality + "connectionStatus=" + connectionStatus + "}";
    }
}