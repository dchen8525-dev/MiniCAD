package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CAPABILITY_DEFINITION.
 * A capability definition entity.
 *
 * @param id STEP instance id
 * @param name capability name
 * @param capabilityType capability variance type
 * @param capabilityDescription capability variance description
 * @param capabilityParameters capability variance parameters
 * @param capabilityLevel capability variance level
 * @param capabilityStatus capability variance status
 */
public record StepCapabilityDefinition(
    int id,
    String name,
    String capabilityType,
    String capabilityDescription,
    List<String> capabilityParameters,
    int capabilityLevel,
    String capabilityStatus) implements StepEntity {
}