package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepRepositoryInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity repositoryDefinition;
    private final String repositoryState;
    private final int repositoryItemCount;
    private final StepEntity repositoryLastSync;
    private final String repositoryStatus;

    public StepRepositoryInstance(int id, String name, StepEntity repositoryDefinition, String repositoryState, int repositoryItemCount, StepEntity repositoryLastSync, String repositoryStatus) {
        this.id = id;
        this.name = name;
        this.repositoryDefinition = repositoryDefinition;
        this.repositoryState = repositoryState;
        this.repositoryItemCount = repositoryItemCount;
        this.repositoryLastSync = repositoryLastSync;
        this.repositoryStatus = repositoryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getRepositoryDefinition() {
        return repositoryDefinition;
    }

    public String getRepositoryState() {
        return repositoryState;
    }

    public int getRepositoryItemCount() {
        return repositoryItemCount;
    }

    public StepEntity getRepositoryLastSync() {
        return repositoryLastSync;
    }

    public String getRepositoryStatus() {
        return repositoryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRepositoryInstance that = (StepRepositoryInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(repositoryDefinition, that.repositoryDefinition) && Objects.equals(repositoryState, that.repositoryState) && repositoryItemCount == that.repositoryItemCount && Objects.equals(repositoryLastSync, that.repositoryLastSync) && Objects.equals(repositoryStatus, that.repositoryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, repositoryDefinition, repositoryState, repositoryItemCount, repositoryLastSync, repositoryStatus);
    }

    @Override
    public String toString() {
        return "StepRepositoryInstance{" + "id=" + id + "name=" + name + "repositoryDefinition=" + repositoryDefinition + "repositoryState=" + repositoryState + "repositoryItemCount=" + repositoryItemCount + "repositoryLastSync=" + repositoryLastSync + "repositoryStatus=" + repositoryStatus + "}";
    }
}