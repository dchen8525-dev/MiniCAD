package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ENVIRONMENT_INSTANCE.
 * An environment instance entity.
 *
 * @param id STEP instance id
 * @param name environment instance name
 * @param environmentDefinition environment variance definition reference
 * @param environmentState environment variance state
 * @param environmentVariables environment variance current variables
 * @param environmentActive environment variance active flag
 * @param environmentStatus environment variance status
 */
public record StepEnvironmentInstance(
    int id,
    String name,
    StepEntity environmentDefinition,
    String environmentState,
    List<String> environmentVariables,
    boolean environmentActive,
    String environmentStatus) implements StepEntity {
}