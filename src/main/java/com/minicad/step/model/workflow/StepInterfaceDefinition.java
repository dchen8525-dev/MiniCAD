package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INTERFACE_DEFINITION.
 * An interface definition entity.
 *
 * @param id STEP instance id
 * @param name interface name
 * @param interfaceType interface variance type
 * @param interfaceProtocol interface variance protocol
 * @param interfaceParameters interface variance parameters
 * @param interfaceConstraints interface variance constraints
 * @param interfaceStatus interface variance status
 */
/**
 * Resolved INTERFACE_DEFINITION.
 * An interface definition entity.
 *
 * @param id STEP instance id
 * @param name interface name
 * @param interfaceType interface variance type
 * @param interfaceProtocol interface variance protocol
 * @param interfaceParameters interface variance parameters
 * @param interfaceConstraints interface variance constraints
 * @param interfaceStatus interface variance status
 */
public final class StepInterfaceDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String interfaceType;
    private final String interfaceProtocol;
    private final List<String> interfaceParameters;
    private final List<String> interfaceConstraints;
    private final String interfaceStatus;

    public StepInterfaceDefinition(int id, String name, String interfaceType, String interfaceProtocol, List<String> interfaceParameters, List<String> interfaceConstraints, String interfaceStatus) {
        this.id = id;
        this.name = name;
        this.interfaceType = interfaceType;
        this.interfaceProtocol = interfaceProtocol;
        this.interfaceParameters = interfaceParameters == null ? null : java.util.List.copyOf(interfaceParameters);
        this.interfaceConstraints = interfaceConstraints == null ? null : java.util.List.copyOf(interfaceConstraints);
        this.interfaceStatus = interfaceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public String getInterfaceProtocol() {
        return interfaceProtocol;
    }

    public List<String> getInterfaceParameters() {
        return interfaceParameters;
    }

    public List<String> getInterfaceConstraints() {
        return interfaceConstraints;
    }

    public String getInterfaceStatus() {
        return interfaceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInterfaceDefinition that = (StepInterfaceDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(interfaceType, that.interfaceType) && Objects.equals(interfaceProtocol, that.interfaceProtocol) && Objects.equals(interfaceParameters, that.interfaceParameters) && Objects.equals(interfaceConstraints, that.interfaceConstraints) && Objects.equals(interfaceStatus, that.interfaceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, interfaceType, interfaceProtocol, interfaceParameters, interfaceConstraints, interfaceStatus);
    }

    @Override
    public String toString() {
        return "StepInterfaceDefinition{" + "id=" + id + "name=" + name + "interfaceType=" + interfaceType + "interfaceProtocol=" + interfaceProtocol + "interfaceParameters=" + interfaceParameters + "interfaceConstraints=" + interfaceConstraints + "interfaceStatus=" + interfaceStatus + "}";
    }
}