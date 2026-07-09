package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONFIGURATION_MANAGEMENT.
 * A configuration management entity.
 *
 * @param id STEP instance id
 * @param name configuration name
 * @param configurationId configuration identifier
 * @param configurationItems configuration items
 * @param configurationStatus configuration status
 * @param configurationBaseline configuration baseline reference
 * @param configurationOwner configuration owner
 */
/**
 * Resolved CONFIGURATION_MANAGEMENT.
 * A configuration management entity.
 *
 * @param id STEP instance id
 * @param name configuration name
 * @param configurationId configuration identifier
 * @param configurationItems configuration items
 * @param configurationStatus configuration status
 * @param configurationBaseline configuration baseline reference
 * @param configurationOwner configuration owner
 */
public final class StepConfigurationManagement implements StepEntity {
    private final int id;
    private final String name;
    private final String configurationId;
    private final List<StepEntity> configurationItems;
    private final String configurationStatus;
    private final StepEntity configurationBaseline;
    private final StepEntity configurationOwner;

    public StepConfigurationManagement(int id, String name, String configurationId, List<StepEntity> configurationItems, String configurationStatus, StepEntity configurationBaseline, StepEntity configurationOwner) {
        this.id = id;
        this.name = name;
        this.configurationId = configurationId;
        this.configurationItems = configurationItems == null ? null : java.util.List.copyOf(configurationItems);
        this.configurationStatus = configurationStatus;
        this.configurationBaseline = configurationBaseline;
        this.configurationOwner = configurationOwner;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConfigurationId() {
        return configurationId;
    }

    public List<StepEntity> getConfigurationItems() {
        return configurationItems;
    }

    public String getConfigurationStatus() {
        return configurationStatus;
    }

    public StepEntity getConfigurationBaseline() {
        return configurationBaseline;
    }

    public StepEntity getConfigurationOwner() {
        return configurationOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConfigurationManagement that = (StepConfigurationManagement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(configurationId, that.configurationId) && Objects.equals(configurationItems, that.configurationItems) && Objects.equals(configurationStatus, that.configurationStatus) && Objects.equals(configurationBaseline, that.configurationBaseline) && Objects.equals(configurationOwner, that.configurationOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, configurationId, configurationItems, configurationStatus, configurationBaseline, configurationOwner);
    }

    @Override
    public String toString() {
        return "StepConfigurationManagement{" + "id=" + id + "name=" + name + "configurationId=" + configurationId + "configurationItems=" + configurationItems + "configurationStatus=" + configurationStatus + "configurationBaseline=" + configurationBaseline + "configurationOwner=" + configurationOwner + "}";
    }
}