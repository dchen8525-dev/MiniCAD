package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepConfigurationManagement extends AbstractStepEntity {
    private final String configurationId;
    private final List<StepEntity> configurationItems;
    private final String configurationStatus;
    private final StepEntity configurationBaseline;
    private final StepEntity configurationOwner;

    public StepConfigurationManagement(int id, String name, String configurationId, List<StepEntity> configurationItems, String configurationStatus, StepEntity configurationBaseline, StepEntity configurationOwner) {
        super(id, name);
        this.configurationId = configurationId;
        this.configurationItems = configurationItems == null ? null : java.util.List.copyOf(configurationItems);
        this.configurationStatus = configurationStatus;
        this.configurationBaseline = configurationBaseline;
        this.configurationOwner = configurationOwner;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("configurationId", configurationId);
        state.put("configurationItems", configurationItems);
        state.put("configurationStatus", configurationStatus);
        state.put("configurationBaseline", configurationBaseline);
        state.put("configurationOwner", configurationOwner);
        return state;
    }
}
