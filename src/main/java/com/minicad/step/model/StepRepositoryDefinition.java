package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepRepositoryDefinition extends AbstractStepEntity {
    private final String repositoryType;
    private final String repositoryDescription;
    private final List<StepEntity> repositoryContents;
    private final String repositoryPolicy;
    private final String repositoryStatus;

    public StepRepositoryDefinition(int id, String name, String repositoryType, String repositoryDescription, List<StepEntity> repositoryContents, String repositoryPolicy, String repositoryStatus) {
        super(id, name);
        this.repositoryType = repositoryType;
        this.repositoryDescription = repositoryDescription;
        this.repositoryContents = repositoryContents == null ? null : java.util.List.copyOf(repositoryContents);
        this.repositoryPolicy = repositoryPolicy;
        this.repositoryStatus = repositoryStatus;
    }

    public String getRepositoryType() {
        return repositoryType;
    }

    public String getRepositoryDescription() {
        return repositoryDescription;
    }

    public List<StepEntity> getRepositoryContents() {
        return repositoryContents;
    }

    public String getRepositoryPolicy() {
        return repositoryPolicy;
    }

    public String getRepositoryStatus() {
        return repositoryStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("repositoryType", repositoryType);
        state.put("repositoryDescription", repositoryDescription);
        state.put("repositoryContents", repositoryContents);
        state.put("repositoryPolicy", repositoryPolicy);
        state.put("repositoryStatus", repositoryStatus);
        return state;
    }
}
