package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ARTIFACT_DEFINITION.
 * An artifact definition entity.
 *
 * @param id STEP instance id
 * @param name artifact name
 * @param artifactType artifact variance type
 * @param artifactDescription artifact variance description
 * @param artifactSource artifact variance source reference
 * @param artifactFormat artifact variance format
 * @param artifactStatus artifact variance status
 */
public record StepArtifactDefinition(
    int id,
    String name,
    String artifactType,
    String artifactDescription,
    StepEntity artifactSource,
    String artifactFormat,
    String artifactStatus) implements StepEntity {
}