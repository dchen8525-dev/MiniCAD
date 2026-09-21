package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepArtifactDefinition extends AbstractStepEntity {
    private final String artifactType;
    private final String artifactDescription;
    private final StepEntity artifactSource;
    private final String artifactFormat;
    private final String artifactStatus;

    public StepArtifactDefinition(int id, String name, String artifactType, String artifactDescription, StepEntity artifactSource, String artifactFormat, String artifactStatus) {
        super(id, name);
        this.artifactType = artifactType;
        this.artifactDescription = artifactDescription;
        this.artifactSource = artifactSource;
        this.artifactFormat = artifactFormat;
        this.artifactStatus = artifactStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("artifactType", artifactType);
        state.put("artifactDescription", artifactDescription);
        state.put("artifactSource", artifactSource);
        state.put("artifactFormat", artifactFormat);
        state.put("artifactStatus", artifactStatus);
        return state;
    }
}
