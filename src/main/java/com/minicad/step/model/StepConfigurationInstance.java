package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONFIGURATION_INSTANCE.
 * A configuration instance entity.
 *
 * @param id STEP instance id
 * @param name configuration instance name
 * @param configurationDefinition configuration variance definition reference
 * @param configurationState configuration variance state
 * @param configurationValues configuration variance current values
 * @param configurationApplied configuration variance applied flag
 * @param configurationStatus configuration variance status
 */
/**
 * Resolved CONFIGURATION_INSTANCE.
 * A configuration instance entity.
 *
 * @param id STEP instance id
 * @param name configuration instance name
 * @param configurationDefinition configuration variance definition reference
 * @param configurationState configuration variance state
 * @param configurationValues configuration variance current values
 * @param configurationApplied configuration variance applied flag
 * @param configurationStatus configuration variance status
 */
public final class StepConfigurationInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity configurationDefinition;
    private final String configurationState;
    private final List<String> configurationValues;
    private final boolean configurationApplied;
    private final String configurationStatus;

    public StepConfigurationInstance(int id, String name, StepEntity configurationDefinition, String configurationState, List<String> configurationValues, boolean configurationApplied, String configurationStatus) {
        this.id = id;
        this.name = name;
        this.configurationDefinition = configurationDefinition;
        this.configurationState = configurationState;
        this.configurationValues = configurationValues == null ? null : java.util.List.copyOf(configurationValues);
        this.configurationApplied = configurationApplied;
        this.configurationStatus = configurationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getConfigurationDefinition() {
        return configurationDefinition;
    }

    public String getConfigurationState() {
        return configurationState;
    }

    public List<String> getConfigurationValues() {
        return configurationValues;
    }

    public boolean isConfigurationApplied() {
        return configurationApplied;
    }

    public String getConfigurationStatus() {
        return configurationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConfigurationInstance that = (StepConfigurationInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(configurationDefinition, that.configurationDefinition) && Objects.equals(configurationState, that.configurationState) && Objects.equals(configurationValues, that.configurationValues) && configurationApplied == that.configurationApplied && Objects.equals(configurationStatus, that.configurationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, configurationDefinition, configurationState, configurationValues, configurationApplied, configurationStatus);
    }

    @Override
    public String toString() {
        return "StepConfigurationInstance{" + "id=" + id + "name=" + name + "configurationDefinition=" + configurationDefinition + "configurationState=" + configurationState + "configurationValues=" + configurationValues + "configurationApplied=" + configurationApplied + "configurationStatus=" + configurationStatus + "}";
    }
}