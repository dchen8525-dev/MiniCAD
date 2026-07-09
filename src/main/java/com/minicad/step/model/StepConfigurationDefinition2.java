package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONFIGURATION_DEFINITION.
 * A configuration definition entity.
 *
 * @param id STEP instance id
 * @param name configuration name
 * @param configurationType configuration variance type
 * @param configurationDescription configuration variance description
 * @param configurationParameters configuration variance parameters
 * @param configurationDefaults configuration variance defaults
 * @param configurationStatus configuration variance status
 */
/**
 * Resolved CONFIGURATION_DEFINITION.
 * A configuration definition entity.
 *
 * @param id STEP instance id
 * @param name configuration name
 * @param configurationType configuration variance type
 * @param configurationDescription configuration variance description
 * @param configurationParameters configuration variance parameters
 * @param configurationDefaults configuration variance defaults
 * @param configurationStatus configuration variance status
 */
public final class StepConfigurationDefinition2 implements StepEntity {
    private final int id;
    private final String name;
    private final String configurationType;
    private final String configurationDescription;
    private final List<String> configurationParameters;
    private final List<String> configurationDefaults;
    private final String configurationStatus;

    public StepConfigurationDefinition2(int id, String name, String configurationType, String configurationDescription, List<String> configurationParameters, List<String> configurationDefaults, String configurationStatus) {
        this.id = id;
        this.name = name;
        this.configurationType = configurationType;
        this.configurationDescription = configurationDescription;
        this.configurationParameters = configurationParameters == null ? null : java.util.List.copyOf(configurationParameters);
        this.configurationDefaults = configurationDefaults == null ? null : java.util.List.copyOf(configurationDefaults);
        this.configurationStatus = configurationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConfigurationType() {
        return configurationType;
    }

    public String getConfigurationDescription() {
        return configurationDescription;
    }

    public List<String> getConfigurationParameters() {
        return configurationParameters;
    }

    public List<String> getConfigurationDefaults() {
        return configurationDefaults;
    }

    public String getConfigurationStatus() {
        return configurationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConfigurationDefinition2 that = (StepConfigurationDefinition2) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(configurationType, that.configurationType) && Objects.equals(configurationDescription, that.configurationDescription) && Objects.equals(configurationParameters, that.configurationParameters) && Objects.equals(configurationDefaults, that.configurationDefaults) && Objects.equals(configurationStatus, that.configurationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, configurationType, configurationDescription, configurationParameters, configurationDefaults, configurationStatus);
    }

    @Override
    public String toString() {
        return "StepConfigurationDefinition2{" + "id=" + id + "name=" + name + "configurationType=" + configurationType + "configurationDescription=" + configurationDescription + "configurationParameters=" + configurationParameters + "configurationDefaults=" + configurationDefaults + "configurationStatus=" + configurationStatus + "}";
    }
}