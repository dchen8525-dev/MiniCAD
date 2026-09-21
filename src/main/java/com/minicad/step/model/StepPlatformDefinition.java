package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPlatformDefinition extends AbstractStepEntity {
    private final String platformType;
    private final String platformDescription;
    private final List<StepEntity> platformComponents;
    private final List<String> platformCapabilities;
    private final String platformStatus;

    public StepPlatformDefinition(int id, String name, String platformType, String platformDescription, List<StepEntity> platformComponents, List<String> platformCapabilities, String platformStatus) {
        super(id, name);
        this.platformType = platformType;
        this.platformDescription = platformDescription;
        this.platformComponents = platformComponents == null ? null : java.util.List.copyOf(platformComponents);
        this.platformCapabilities = platformCapabilities == null ? null : java.util.List.copyOf(platformCapabilities);
        this.platformStatus = platformStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("platformType", platformType);
        state.put("platformDescription", platformDescription);
        state.put("platformComponents", platformComponents);
        state.put("platformCapabilities", platformCapabilities);
        state.put("platformStatus", platformStatus);
        return state;
    }
}
