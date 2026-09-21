package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ENVIRONMENT_DEFINITION.
 * An environment definition entity.
 *
 * @param id STEP instance id
 * @param name environment name
 * @param environmentType environment variance type
 * @param environmentDescription environment variance description
 * @param environmentParameters environment variance parameters
 * @param environmentConstraints environment variance constraints
 * @param environmentStatus environment variance status
 */
public final class StepEnvironmentDefinition extends AbstractStepEntity {
    private final String environmentType;
    private final String environmentDescription;
    private final List<String> environmentParameters;
    private final List<String> environmentConstraints;
    private final String environmentStatus;

    public StepEnvironmentDefinition(int id, String name, String environmentType, String environmentDescription, List<String> environmentParameters, List<String> environmentConstraints, String environmentStatus) {
        super(id, name);
        this.environmentType = environmentType;
        this.environmentDescription = environmentDescription;
        this.environmentParameters = environmentParameters == null ? null : java.util.List.copyOf(environmentParameters);
        this.environmentConstraints = environmentConstraints == null ? null : java.util.List.copyOf(environmentConstraints);
        this.environmentStatus = environmentStatus;
    }

    public String getEnvironmentType() {
        return environmentType;
    }

    public String getEnvironmentDescription() {
        return environmentDescription;
    }

    public List<String> getEnvironmentParameters() {
        return environmentParameters;
    }

    public List<String> getEnvironmentConstraints() {
        return environmentConstraints;
    }

    public String getEnvironmentStatus() {
        return environmentStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("environmentType", environmentType);
        state.put("environmentDescription", environmentDescription);
        state.put("environmentParameters", environmentParameters);
        state.put("environmentConstraints", environmentConstraints);
        state.put("environmentStatus", environmentStatus);
        return state;
    }
}
