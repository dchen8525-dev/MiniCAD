package com.minicad.step.model;

import java.util.List;

/**
 * Resolved REPOSITORY_DEFINITION.
 * A repository definition entity.
 *
 * @param id STEP instance id
 * @param name repository name
 * @param repositoryType repository variance type
 * @param repositoryDescription repository variance description
 * @param repositoryContents repository variance content definitions
 * @param repositoryPolicy repository variance policy
 * @param repositoryStatus repository variance status
 */
public record StepRepositoryDefinition(
    int id,
    String name,
    String repositoryType,
    String repositoryDescription,
    List<StepEntity> repositoryContents,
    String repositoryPolicy,
    String repositoryStatus) implements StepEntity {
}