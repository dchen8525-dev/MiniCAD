package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ENVIRONMENT_DEFINITION.
 * An environment definition entity.
 *
 * @param id STEP instance id
 * @param name environment name
 * @param environmentType environment variance type
 * @param environmentDescription environment variance description
 * @param environmentParameters environment variance parameters
 * @param environmentConstraints environment variance constraints
 * @param environmentStatus environment variance status
 */
/**
 * Resolved ENVIRONMENT_DEFINITION.
 * An environment definition entity.
 *
 * @param id STEP instance id
 * @param name environment name
 * @param environmentType environment variance type
 * @param environmentDescription environment variance description
 * @param environmentParameters environment variance parameters
 * @param environmentConstraints environment variance constraints
 * @param environmentStatus environment variance status
 */
public final class StepEnvironmentDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String environmentType;
    private final String environmentDescription;
    private final List<String> environmentParameters;
    private final List<String> environmentConstraints;
    private final String environmentStatus;

    public StepEnvironmentDefinition(int id, String name, String environmentType, String environmentDescription, List<String> environmentParameters, List<String> environmentConstraints, String environmentStatus) {
        this.id = id;
        this.name = name;
        this.environmentType = environmentType;
        this.environmentDescription = environmentDescription;
        this.environmentParameters = environmentParameters == null ? null : java.util.List.copyOf(environmentParameters);
        this.environmentConstraints = environmentConstraints == null ? null : java.util.List.copyOf(environmentConstraints);
        this.environmentStatus = environmentStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEnvironmentType() {
        return environmentType;
    }

    public String getEnvironmentDescription() {
        return environmentDescription;
    }

    public List<String> getEnvironmentParameters() {
        return environmentParameters;
    }

    public List<String> getEnvironmentConstraints() {
        return environmentConstraints;
    }

    public String getEnvironmentStatus() {
        return environmentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEnvironmentDefinition that = (StepEnvironmentDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(environmentType, that.environmentType) && Objects.equals(environmentDescription, that.environmentDescription) && Objects.equals(environmentParameters, that.environmentParameters) && Objects.equals(environmentConstraints, that.environmentConstraints) && Objects.equals(environmentStatus, that.environmentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, environmentType, environmentDescription, environmentParameters, environmentConstraints, environmentStatus);
    }

    @Override
    public String toString() {
        return "StepEnvironmentDefinition{" + "id=" + id + "name=" + name + "environmentType=" + environmentType + "environmentDescription=" + environmentDescription + "environmentParameters=" + environmentParameters + "environmentConstraints=" + environmentConstraints + "environmentStatus=" + environmentStatus + "}";
    }
}