package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepResourceDefinition(
    int id,
    String name,
    String resourceType,
    String resourceCategory,
    List<String> resourceCapabilities,
    List<String> resourceConstraints,
    String resourceStatus) implements StepEntity {
}