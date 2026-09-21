package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepConfigurationInstance extends AbstractStepEntity {
    private final StepEntity configurationDefinition;
    private final String configurationState;
    private final List<String> configurationValues;
    private final boolean configurationApplied;
    private final String configurationStatus;

    public StepConfigurationInstance(int id, String name, StepEntity configurationDefinition, String configurationState, List<String> configurationValues, boolean configurationApplied, String configurationStatus) {
        super(id, name);
        this.configurationDefinition = configurationDefinition;
        this.configurationState = configurationState;
        this.configurationValues = configurationValues == null ? null : java.util.List.copyOf(configurationValues);
        this.configurationApplied = configurationApplied;
        this.configurationStatus = configurationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("configurationDefinition", configurationDefinition);
        state.put("configurationState", configurationState);
        state.put("configurationValues", configurationValues);
        state.put("configurationApplied", configurationApplied);
        state.put("configurationStatus", configurationStatus);
        return state;
    }
}
