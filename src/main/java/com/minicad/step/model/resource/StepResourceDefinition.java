package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepResourceDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String resourceType;
    private final String resourceCategory;
    private final List<String> resourceCapabilities;
    private final List<String> resourceConstraints;
    private final String resourceStatus;

    public StepResourceDefinition(int id, String name, String resourceType, String resourceCategory, List<String> resourceCapabilities, List<String> resourceConstraints, String resourceStatus) {
        this.id = id;
        this.name = name;
        this.resourceType = resourceType;
        this.resourceCategory = resourceCategory;
        this.resourceCapabilities = resourceCapabilities == null ? null : java.util.List.copyOf(resourceCapabilities);
        this.resourceConstraints = resourceConstraints == null ? null : java.util.List.copyOf(resourceConstraints);
        this.resourceStatus = resourceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepResourceDefinition that = (StepResourceDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(resourceType, that.resourceType) && Objects.equals(resourceCategory, that.resourceCategory) && Objects.equals(resourceCapabilities, that.resourceCapabilities) && Objects.equals(resourceConstraints, that.resourceConstraints) && Objects.equals(resourceStatus, that.resourceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, resourceType, resourceCategory, resourceCapabilities, resourceConstraints, resourceStatus);
    }

    @Override
    public String toString() {
        return "StepResourceDefinition{" + "id=" + id + "name=" + name + "resourceType=" + resourceType + "resourceCategory=" + resourceCategory + "resourceCapabilities=" + resourceCapabilities + "resourceConstraints=" + resourceConstraints + "resourceStatus=" + resourceStatus + "}";
    }
}