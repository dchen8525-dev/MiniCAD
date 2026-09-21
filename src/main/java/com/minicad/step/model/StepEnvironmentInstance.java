package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ENVIRONMENT_INSTANCE.
 * An environment instance entity.
 *
 * @param id STEP instance id
 * @param name environment instance name
 * @param environmentDefinition environment variance definition reference
 * @param environmentState environment variance state
 * @param environmentVariables environment variance current variables
 * @param environmentActive environment variance active flag
 * @param environmentStatus environment variance status
 */
public final class StepEnvironmentInstance extends AbstractStepEntity {
    private final StepEntity environmentDefinition;
    private final String environmentState;
    private final List<String> environmentVariables;
    private final boolean environmentActive;
    private final String environmentStatus;

    public StepEnvironmentInstance(int id, String name, StepEntity environmentDefinition, String environmentState, List<String> environmentVariables, boolean environmentActive, String environmentStatus) {
        super(id, name);
        this.environmentDefinition = environmentDefinition;
        this.environmentState = environmentState;
        this.environmentVariables = environmentVariables == null ? null : java.util.List.copyOf(environmentVariables);
        this.environmentActive = environmentActive;
        this.environmentStatus = environmentStatus;
    }

    public StepEntity getEnvironmentDefinition() {
        return environmentDefinition;
    }

    public String getEnvironmentState() {
        return environmentState;
    }

    public List<String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public boolean isEnvironmentActive() {
        return environmentActive;
    }

    public String getEnvironmentStatus() {
        return environmentStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("environmentDefinition", environmentDefinition);
        state.put("environmentState", environmentState);
        state.put("environmentVariables", environmentVariables);
        state.put("environmentActive", environmentActive);
        state.put("environmentStatus", environmentStatus);
        return state;
    }
}
