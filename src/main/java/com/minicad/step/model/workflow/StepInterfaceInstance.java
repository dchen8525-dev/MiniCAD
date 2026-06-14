package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INTERFACE_INSTANCE.
 * An interface instance entity.
 *
 * @param id STEP instance id
 * @param name interface instance name
 * @param interfaceDefinition interface variance definition reference
 * @param interfaceLocation interface variance location reference
 * @param interfaceState interface variance state
 * @param interfaceConnections interface variance connections
 * @param interfaceStatus interface variance status
 */
/**
 * Resolved INTERFACE_INSTANCE.
 * An interface instance entity.
 *
 * @param id STEP instance id
 * @param name interface instance name
 * @param interfaceDefinition interface variance definition reference
 * @param interfaceLocation interface variance location reference
 * @param interfaceState interface variance state
 * @param interfaceConnections interface variance connections
 * @param interfaceStatus interface variance status
 */
public final class StepInterfaceInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity interfaceDefinition;
    private final StepEntity interfaceLocation;
    private final String interfaceState;
    private final List<StepEntity> interfaceConnections;
    private final String interfaceStatus;

    public StepInterfaceInstance(int id, String name, StepEntity interfaceDefinition, StepEntity interfaceLocation, String interfaceState, List<StepEntity> interfaceConnections, String interfaceStatus) {
        this.id = id;
        this.name = name;
        this.interfaceDefinition = interfaceDefinition;
        this.interfaceLocation = interfaceLocation;
        this.interfaceState = interfaceState;
        this.interfaceConnections = interfaceConnections == null ? null : java.util.List.copyOf(interfaceConnections);
        this.interfaceStatus = interfaceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getInterfaceDefinition() {
        return interfaceDefinition;
    }

    public StepEntity getInterfaceLocation() {
        return interfaceLocation;
    }

    public String getInterfaceState() {
        return interfaceState;
    }

    public List<StepEntity> getInterfaceConnections() {
        return interfaceConnections;
    }

    public String getInterfaceStatus() {
        return interfaceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInterfaceInstance that = (StepInterfaceInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(interfaceDefinition, that.interfaceDefinition) && Objects.equals(interfaceLocation, that.interfaceLocation) && Objects.equals(interfaceState, that.interfaceState) && Objects.equals(interfaceConnections, that.interfaceConnections) && Objects.equals(interfaceStatus, that.interfaceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, interfaceDefinition, interfaceLocation, interfaceState, interfaceConnections, interfaceStatus);
    }

    @Override
    public String toString() {
        return "StepInterfaceInstance{" + "id=" + id + "name=" + name + "interfaceDefinition=" + interfaceDefinition + "interfaceLocation=" + interfaceLocation + "interfaceState=" + interfaceState + "interfaceConnections=" + interfaceConnections + "interfaceStatus=" + interfaceStatus + "}";
    }
}