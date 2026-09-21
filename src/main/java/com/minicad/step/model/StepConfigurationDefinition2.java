package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepConfigurationDefinition2 extends AbstractStepEntity {
    private final String configurationType;
    private final String configurationDescription;
    private final List<String> configurationParameters;
    private final List<String> configurationDefaults;
    private final String configurationStatus;

    public StepConfigurationDefinition2(int id, String name, String configurationType, String configurationDescription, List<String> configurationParameters, List<String> configurationDefaults, String configurationStatus) {
        super(id, name);
        this.configurationType = configurationType;
        this.configurationDescription = configurationDescription;
        this.configurationParameters = configurationParameters == null ? null : java.util.List.copyOf(configurationParameters);
        this.configurationDefaults = configurationDefaults == null ? null : java.util.List.copyOf(configurationDefaults);
        this.configurationStatus = configurationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("configurationType", configurationType);
        state.put("configurationDescription", configurationDescription);
        state.put("configurationParameters", configurationParameters);
        state.put("configurationDefaults", configurationDefaults);
        state.put("configurationStatus", configurationStatus);
        return state;
    }
}
