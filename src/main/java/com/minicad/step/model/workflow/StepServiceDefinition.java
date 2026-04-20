package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SERVICE_DEFINITION.
 * A service definition entity.
 *
 * @param id STEP instance id
 * @param name service name
 * @param serviceType service variance type
 * @param serviceDescription service variance description
 * @param serviceInterface service variance interface reference
 * @param serviceDependencies service variance dependencies
 * @param serviceStatus service variance status
 */
public record StepServiceDefinition(
    int id,
    String name,
    String serviceType,
    String serviceDescription,
    StepEntity serviceInterface,
    List<StepEntity> serviceDependencies,
    String serviceStatus) implements StepEntity {
}