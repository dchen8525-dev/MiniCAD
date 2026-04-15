package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ARTIFACT_INSTANCE.
 * An artifact instance entity.
 *
 * @param id STEP instance id
 * @param name artifact instance name
 * @param artifactDefinition artifact variance definition reference
 * @param artifactState artifact variance state
 * @param artifactLocation artifact variance location reference
 * @param artifactSize artifact variance size
 * @param artifactStatus artifact variance status
 */
public record StepArtifactInstance(
    int id,
    String name,
    StepEntity artifactDefinition,
    String artifactState,
    StepEntity artifactLocation,
    long artifactSize,
    String artifactStatus) implements StepEntity {
}