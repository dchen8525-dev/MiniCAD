package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ENVIRONMENT_INSTANCE.
 * An environment instance entity.
 *
 * @param id STEP instance id
 * @param name environment instance name
 * @param environmentDefinition environment variance definition reference
 * @param environmentState environment variance state
 * @param environmentVariables environment variance current variables
 * @param environmentActive environment variance active flag
 * @param environmentStatus environment variance status
 */
/**
 * Resolved ENVIRONMENT_INSTANCE.
 * An environment instance entity.
 *
 * @param id STEP instance id
 * @param name environment instance name
 * @param environmentDefinition environment variance definition reference
 * @param environmentState environment variance state
 * @param environmentVariables environment variance current variables
 * @param environmentActive environment variance active flag
 * @param environmentStatus environment variance status
 */
public final class StepEnvironmentInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity environmentDefinition;
    private final String environmentState;
    private final List<String> environmentVariables;
    private final boolean environmentActive;
    private final String environmentStatus;

    public StepEnvironmentInstance(int id, String name, StepEntity environmentDefinition, String environmentState, List<String> environmentVariables, boolean environmentActive, String environmentStatus) {
        this.id = id;
        this.name = name;
        this.environmentDefinition = environmentDefinition;
        this.environmentState = environmentState;
        this.environmentVariables = environmentVariables == null ? null : java.util.List.copyOf(environmentVariables);
        this.environmentActive = environmentActive;
        this.environmentStatus = environmentStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getEnvironmentDefinition() {
        return environmentDefinition;
    }

    public String getEnvironmentState() {
        return environmentState;
    }

    public List<String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public boolean isEnvironmentActive() {
        return environmentActive;
    }

    public String getEnvironmentStatus() {
        return environmentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEnvironmentInstance that = (StepEnvironmentInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(environmentDefinition, that.environmentDefinition) && Objects.equals(environmentState, that.environmentState) && Objects.equals(environmentVariables, that.environmentVariables) && environmentActive == that.environmentActive && Objects.equals(environmentStatus, that.environmentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, environmentDefinition, environmentState, environmentVariables, environmentActive, environmentStatus);
    }

    @Override
    public String toString() {
        return "StepEnvironmentInstance{" + "id=" + id + "name=" + name + "environmentDefinition=" + environmentDefinition + "environmentState=" + environmentState + "environmentVariables=" + environmentVariables + "environmentActive=" + environmentActive + "environmentStatus=" + environmentStatus + "}";
    }
}