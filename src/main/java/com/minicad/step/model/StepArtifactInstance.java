package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepArtifactInstance extends AbstractStepEntity {
    private final StepEntity artifactDefinition;
    private final String artifactState;
    private final StepEntity artifactLocation;
    private final long artifactSize;
    private final String artifactStatus;

    public StepArtifactInstance(int id, String name, StepEntity artifactDefinition, String artifactState, StepEntity artifactLocation, long artifactSize, String artifactStatus) {
        super(id, name);
        this.artifactDefinition = artifactDefinition;
        this.artifactState = artifactState;
        this.artifactLocation = artifactLocation;
        this.artifactSize = artifactSize;
        this.artifactStatus = artifactStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("artifactDefinition", artifactDefinition);
        state.put("artifactState", artifactState);
        state.put("artifactLocation", artifactLocation);
        state.put("artifactSize", artifactSize);
        state.put("artifactStatus", artifactStatus);
        return state;
    }
}
