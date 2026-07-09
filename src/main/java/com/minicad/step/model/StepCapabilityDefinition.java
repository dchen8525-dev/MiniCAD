package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CAPABILITY_DEFINITION.
 * A capability definition entity.
 *
 * @param id STEP instance id
 * @param name capability name
 * @param capabilityType capability variance type
 * @param capabilityDescription capability variance description
 * @param capabilityParameters capability variance parameters
 * @param capabilityLevel capability variance level
 * @param capabilityStatus capability variance status
 */
/**
 * Resolved CAPABILITY_DEFINITION.
 * A capability definition entity.
 *
 * @param id STEP instance id
 * @param name capability name
 * @param capabilityType capability variance type
 * @param capabilityDescription capability variance description
 * @param capabilityParameters capability variance parameters
 * @param capabilityLevel capability variance level
 * @param capabilityStatus capability variance status
 */
public final class StepCapabilityDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String capabilityType;
    private final String capabilityDescription;
    private final List<String> capabilityParameters;
    private final int capabilityLevel;
    private final String capabilityStatus;

    public StepCapabilityDefinition(int id, String name, String capabilityType, String capabilityDescription, List<String> capabilityParameters, int capabilityLevel, String capabilityStatus) {
        this.id = id;
        this.name = name;
        this.capabilityType = capabilityType;
        this.capabilityDescription = capabilityDescription;
        this.capabilityParameters = capabilityParameters == null ? null : java.util.List.copyOf(capabilityParameters);
        this.capabilityLevel = capabilityLevel;
        this.capabilityStatus = capabilityStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCapabilityType() {
        return capabilityType;
    }

    public String getCapabilityDescription() {
        return capabilityDescription;
    }

    public List<String> getCapabilityParameters() {
        return capabilityParameters;
    }

    public int getCapabilityLevel() {
        return capabilityLevel;
    }

    public String getCapabilityStatus() {
        return capabilityStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCapabilityDefinition that = (StepCapabilityDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(capabilityType, that.capabilityType) && Objects.equals(capabilityDescription, that.capabilityDescription) && Objects.equals(capabilityParameters, that.capabilityParameters) && capabilityLevel == that.capabilityLevel && Objects.equals(capabilityStatus, that.capabilityStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, capabilityType, capabilityDescription, capabilityParameters, capabilityLevel, capabilityStatus);
    }

    @Override
    public String toString() {
        return "StepCapabilityDefinition{" + "id=" + id + "name=" + name + "capabilityType=" + capabilityType + "capabilityDescription=" + capabilityDescription + "capabilityParameters=" + capabilityParameters + "capabilityLevel=" + capabilityLevel + "capabilityStatus=" + capabilityStatus + "}";
    }
}