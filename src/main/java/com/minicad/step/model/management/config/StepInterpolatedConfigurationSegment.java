package com.minicad.step.model.management.config;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved INTERPOLATED_CONFIGURATION_SEGMENT.
 * Interpolated configuration segment.
 */
/**
 * Resolved INTERPOLATED_CONFIGURATION_SEGMENT.
 * Interpolated configuration segment.
 */
public final class StepInterpolatedConfigurationSegment implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity configuration;

    public StepInterpolatedConfigurationSegment(int id, String name, String description, StepEntity configuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.configuration = configuration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getConfiguration() {
        return configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInterpolatedConfigurationSegment that = (StepInterpolatedConfigurationSegment) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(configuration, that.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, configuration);
    }

    @Override
    public String toString() {
        return "StepInterpolatedConfigurationSegment{" + "id=" + id + "name=" + name + "description=" + description + "configuration=" + configuration + "}";
    }
}
