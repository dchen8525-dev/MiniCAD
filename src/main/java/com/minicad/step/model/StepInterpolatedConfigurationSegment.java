package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved INTERPOLATED_CONFIGURATION_SEGMENT.
 * Interpolated configuration segment.
 */
public final class StepInterpolatedConfigurationSegment extends AbstractStepEntity {
    private final String description;
    private final StepEntity configuration;

    public StepInterpolatedConfigurationSegment(int id, String name, String description, StepEntity configuration) {
        super(id, name);
        this.description = description;
        this.configuration = configuration;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getConfiguration() {
        return configuration;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("configuration", configuration);
        return state;
    }
}
