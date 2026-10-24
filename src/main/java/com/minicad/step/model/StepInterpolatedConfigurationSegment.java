package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "configuration", configuration);
    }
}
