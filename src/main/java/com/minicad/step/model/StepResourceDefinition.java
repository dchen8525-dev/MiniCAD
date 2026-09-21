package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RESOURCE_DEFINITION.
 * A resource definition entity.
 *
 * @param id STEP instance id
 * @param name resource name
 * @param resourceType resource variance type
 * @param resourceCategory resource variance category
 * @param resourceCapabilities resource variance capabilities
 * @param resourceConstraints resource variance constraints
 * @param resourceStatus resource variance status
 */
public final class StepResourceDefinition extends AbstractStepEntity {
    private final String resourceType;
    private final String resourceCategory;
    private final List<String> resourceCapabilities;
    private final List<String> resourceConstraints;
    private final String resourceStatus;

    public StepResourceDefinition(int id, String name, String resourceType, String resourceCategory, List<String> resourceCapabilities, List<String> resourceConstraints, String resourceStatus) {
        super(id, name);
        this.resourceType = resourceType;
        this.resourceCategory = resourceCategory;
        this.resourceCapabilities = resourceCapabilities == null ? null : java.util.List.copyOf(resourceCapabilities);
        this.resourceConstraints = resourceConstraints == null ? null : java.util.List.copyOf(resourceConstraints);
        this.resourceStatus = resourceStatus;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceCategory() {
        return resourceCategory;
    }

    public List<String> getResourceCapabilities() {
        return resourceCapabilities;
    }

    public List<String> getResourceConstraints() {
        return resourceConstraints;
    }

    public String getResourceStatus() {
        return resourceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("resourceType", resourceType);
        state.put("resourceCategory", resourceCategory);
        state.put("resourceCapabilities", resourceCapabilities);
        state.put("resourceConstraints", resourceConstraints);
        state.put("resourceStatus", resourceStatus);
        return state;
    }
}
