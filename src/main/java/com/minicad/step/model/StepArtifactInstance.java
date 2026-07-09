package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepArtifactInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity artifactDefinition;
    private final String artifactState;
    private final StepEntity artifactLocation;
    private final long artifactSize;
    private final String artifactStatus;

    public StepArtifactInstance(int id, String name, StepEntity artifactDefinition, String artifactState, StepEntity artifactLocation, long artifactSize, String artifactStatus) {
        this.id = id;
        this.name = name;
        this.artifactDefinition = artifactDefinition;
        this.artifactState = artifactState;
        this.artifactLocation = artifactLocation;
        this.artifactSize = artifactSize;
        this.artifactStatus = artifactStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getArtifactDefinition() {
        return artifactDefinition;
    }

    public String getArtifactState() {
        return artifactState;
    }

    public StepEntity getArtifactLocation() {
        return artifactLocation;
    }

    public long getArtifactSize() {
        return artifactSize;
    }

    public String getArtifactStatus() {
        return artifactStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepArtifactInstance that = (StepArtifactInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(artifactDefinition, that.artifactDefinition) && Objects.equals(artifactState, that.artifactState) && Objects.equals(artifactLocation, that.artifactLocation) && artifactSize == that.artifactSize && Objects.equals(artifactStatus, that.artifactStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artifactDefinition, artifactState, artifactLocation, artifactSize, artifactStatus);
    }

    @Override
    public String toString() {
        return "StepArtifactInstance{" + "id=" + id + "name=" + name + "artifactDefinition=" + artifactDefinition + "artifactState=" + artifactState + "artifactLocation=" + artifactLocation + "artifactSize=" + artifactSize + "artifactStatus=" + artifactStatus + "}";
    }
}