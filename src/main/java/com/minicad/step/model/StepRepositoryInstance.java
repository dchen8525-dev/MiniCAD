package com.minicad.step.model;

import java.util.List;

/**
 * Resolved REPOSITORY_INSTANCE.
 * A repository instance entity.
 *
 * @param id STEP instance id
 * @param name repository instance name
 * @param repositoryDefinition repository variance definition reference
 * @param repositoryState repository variance state
 * @param repositoryItemCount repository variance item count
 * @param repositoryLastSync repository variance last sync time
 * @param repositoryStatus repository variance status
 */
public record StepRepositoryInstance(
    int id,
    String name,
    StepEntity repositoryDefinition,
    String repositoryState,
    int repositoryItemCount,
    StepEntity repositoryLastSync,
    String repositoryStatus) implements StepEntity {
}