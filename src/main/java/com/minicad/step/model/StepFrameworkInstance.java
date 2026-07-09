package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FRAMEWORK_INSTANCE.
 * A framework instance entity.
 *
 * @param id STEP instance id
 * @param name framework instance name
 * @param frameworkDefinition framework variance definition reference
 * @param frameworkState framework variance state
 * @param frameworkVersion framework variance version
 * @param frameworkConfig framework variance configuration
 * @param frameworkStatus framework variance status
 */
/**
 * Resolved FRAMEWORK_INSTANCE.
 * A framework instance entity.
 *
 * @param id STEP instance id
 * @param name framework instance name
 * @param frameworkDefinition framework variance definition reference
 * @param frameworkState framework variance state
 * @param frameworkVersion framework variance version
 * @param frameworkConfig framework variance configuration
 * @param frameworkStatus framework variance status
 */
public final class StepFrameworkInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity frameworkDefinition;
    private final String frameworkState;
    private final String frameworkVersion;
    private final List<String> frameworkConfig;
    private final String frameworkStatus;

    public StepFrameworkInstance(int id, String name, StepEntity frameworkDefinition, String frameworkState, String frameworkVersion, List<String> frameworkConfig, String frameworkStatus) {
        this.id = id;
        this.name = name;
        this.frameworkDefinition = frameworkDefinition;
        this.frameworkState = frameworkState;
        this.frameworkVersion = frameworkVersion;
        this.frameworkConfig = frameworkConfig == null ? null : java.util.List.copyOf(frameworkConfig);
        this.frameworkStatus = frameworkStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getFrameworkDefinition() {
        return frameworkDefinition;
    }

    public String getFrameworkState() {
        return frameworkState;
    }

    public String getFrameworkVersion() {
        return frameworkVersion;
    }

    public List<String> getFrameworkConfig() {
        return frameworkConfig;
    }

    public String getFrameworkStatus() {
        return frameworkStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFrameworkInstance that = (StepFrameworkInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(frameworkDefinition, that.frameworkDefinition) && Objects.equals(frameworkState, that.frameworkState) && Objects.equals(frameworkVersion, that.frameworkVersion) && Objects.equals(frameworkConfig, that.frameworkConfig) && Objects.equals(frameworkStatus, that.frameworkStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, frameworkDefinition, frameworkState, frameworkVersion, frameworkConfig, frameworkStatus);
    }

    @Override
    public String toString() {
        return "StepFrameworkInstance{" + "id=" + id + "name=" + name + "frameworkDefinition=" + frameworkDefinition + "frameworkState=" + frameworkState + "frameworkVersion=" + frameworkVersion + "frameworkConfig=" + frameworkConfig + "frameworkStatus=" + frameworkStatus + "}";
    }
}