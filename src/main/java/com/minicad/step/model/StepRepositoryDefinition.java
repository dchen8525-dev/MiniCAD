package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepRepositoryDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String repositoryType;
    private final String repositoryDescription;
    private final List<StepEntity> repositoryContents;
    private final String repositoryPolicy;
    private final String repositoryStatus;

    public StepRepositoryDefinition(int id, String name, String repositoryType, String repositoryDescription, List<StepEntity> repositoryContents, String repositoryPolicy, String repositoryStatus) {
        this.id = id;
        this.name = name;
        this.repositoryType = repositoryType;
        this.repositoryDescription = repositoryDescription;
        this.repositoryContents = repositoryContents == null ? null : java.util.List.copyOf(repositoryContents);
        this.repositoryPolicy = repositoryPolicy;
        this.repositoryStatus = repositoryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRepositoryDefinition that = (StepRepositoryDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(repositoryType, that.repositoryType) && Objects.equals(repositoryDescription, that.repositoryDescription) && Objects.equals(repositoryContents, that.repositoryContents) && Objects.equals(repositoryPolicy, that.repositoryPolicy) && Objects.equals(repositoryStatus, that.repositoryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, repositoryType, repositoryDescription, repositoryContents, repositoryPolicy, repositoryStatus);
    }

    @Override
    public String toString() {
        return "StepRepositoryDefinition{" + "id=" + id + "name=" + name + "repositoryType=" + repositoryType + "repositoryDescription=" + repositoryDescription + "repositoryContents=" + repositoryContents + "repositoryPolicy=" + repositoryPolicy + "repositoryStatus=" + repositoryStatus + "}";
    }
}