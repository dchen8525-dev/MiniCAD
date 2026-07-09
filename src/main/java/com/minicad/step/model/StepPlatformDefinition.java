package com.minicad.step.model.system;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PLATFORM_DEFINITION.
 * A platform definition entity.
 *
 * @param id STEP instance id
 * @param name platform name
 * @param platformType platform variance type
 * @param platformDescription platform variance description
 * @param platformComponents platform variance component definitions
 * @param platformCapabilities platform variance capabilities
 * @param platformStatus platform variance status
 */
/**
 * Resolved PLATFORM_DEFINITION.
 * A platform definition entity.
 *
 * @param id STEP instance id
 * @param name platform name
 * @param platformType platform variance type
 * @param platformDescription platform variance description
 * @param platformComponents platform variance component definitions
 * @param platformCapabilities platform variance capabilities
 * @param platformStatus platform variance status
 */
public final class StepPlatformDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String platformType;
    private final String platformDescription;
    private final List<StepEntity> platformComponents;
    private final List<String> platformCapabilities;
    private final String platformStatus;

    public StepPlatformDefinition(int id, String name, String platformType, String platformDescription, List<StepEntity> platformComponents, List<String> platformCapabilities, String platformStatus) {
        this.id = id;
        this.name = name;
        this.platformType = platformType;
        this.platformDescription = platformDescription;
        this.platformComponents = platformComponents == null ? null : java.util.List.copyOf(platformComponents);
        this.platformCapabilities = platformCapabilities == null ? null : java.util.List.copyOf(platformCapabilities);
        this.platformStatus = platformStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlatformType() {
        return platformType;
    }

    public String getPlatformDescription() {
        return platformDescription;
    }

    public List<StepEntity> getPlatformComponents() {
        return platformComponents;
    }

    public List<String> getPlatformCapabilities() {
        return platformCapabilities;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPlatformDefinition that = (StepPlatformDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(platformType, that.platformType) && Objects.equals(platformDescription, that.platformDescription) && Objects.equals(platformComponents, that.platformComponents) && Objects.equals(platformCapabilities, that.platformCapabilities) && Objects.equals(platformStatus, that.platformStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, platformType, platformDescription, platformComponents, platformCapabilities, platformStatus);
    }

    @Override
    public String toString() {
        return "StepPlatformDefinition{" + "id=" + id + "name=" + name + "platformType=" + platformType + "platformDescription=" + platformDescription + "platformComponents=" + platformComponents + "platformCapabilities=" + platformCapabilities + "platformStatus=" + platformStatus + "}";
    }
}