package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ENVIRONMENT_DEFINITION.
 * An environment definition entity.
 *
 * @param id STEP instance id
 * @param name environment name
 * @param environmentType environment variance type
 * @param environmentDescription environment variance description
 * @param environmentParameters environment variance parameters
 * @param environmentConstraints environment variance constraints
 * @param environmentStatus environment variance status
 */
public record StepEnvironmentDefinition(
    int id,
    String name,
    String environmentType,
    String environmentDescription,
    List<String> environmentParameters,
    List<String> environmentConstraints,
    String environmentStatus) implements StepEntity {
}