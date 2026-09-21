package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepRepositoryInstance extends AbstractStepEntity {
    private final StepEntity repositoryDefinition;
    private final String repositoryState;
    private final int repositoryItemCount;
    private final StepEntity repositoryLastSync;
    private final String repositoryStatus;

    public StepRepositoryInstance(int id, String name, StepEntity repositoryDefinition, String repositoryState, int repositoryItemCount, StepEntity repositoryLastSync, String repositoryStatus) {
        super(id, name);
        this.repositoryDefinition = repositoryDefinition;
        this.repositoryState = repositoryState;
        this.repositoryItemCount = repositoryItemCount;
        this.repositoryLastSync = repositoryLastSync;
        this.repositoryStatus = repositoryStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("repositoryDefinition", repositoryDefinition);
        state.put("repositoryState", repositoryState);
        state.put("repositoryItemCount", repositoryItemCount);
        state.put("repositoryLastSync", repositoryLastSync);
        state.put("repositoryStatus", repositoryStatus);
        return state;
    }
}
