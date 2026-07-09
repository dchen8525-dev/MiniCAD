package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepArtifactDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String artifactType;
    private final String artifactDescription;
    private final StepEntity artifactSource;
    private final String artifactFormat;
    private final String artifactStatus;

    public StepArtifactDefinition(int id, String name, String artifactType, String artifactDescription, StepEntity artifactSource, String artifactFormat, String artifactStatus) {
        this.id = id;
        this.name = name;
        this.artifactType = artifactType;
        this.artifactDescription = artifactDescription;
        this.artifactSource = artifactSource;
        this.artifactFormat = artifactFormat;
        this.artifactStatus = artifactStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtifactType() {
        return artifactType;
    }

    public String getArtifactDescription() {
        return artifactDescription;
    }

    public StepEntity getArtifactSource() {
        return artifactSource;
    }

    public String getArtifactFormat() {
        return artifactFormat;
    }

    public String getArtifactStatus() {
        return artifactStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepArtifactDefinition that = (StepArtifactDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(artifactType, that.artifactType) && Objects.equals(artifactDescription, that.artifactDescription) && Objects.equals(artifactSource, that.artifactSource) && Objects.equals(artifactFormat, that.artifactFormat) && Objects.equals(artifactStatus, that.artifactStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artifactType, artifactDescription, artifactSource, artifactFormat, artifactStatus);
    }

    @Override
    public String toString() {
        return "StepArtifactDefinition{" + "id=" + id + "name=" + name + "artifactType=" + artifactType + "artifactDescription=" + artifactDescription + "artifactSource=" + artifactSource + "artifactFormat=" + artifactFormat + "artifactStatus=" + artifactStatus + "}";
    }
}