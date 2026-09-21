package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FRAMEWORK_INSTANCE.
 * A framework instance entity.
 *
 * @param id STEP instance id
 * @param name framework instance name
 * @param frameworkDefinition framework variance definition reference
 * @param frameworkState framework variance state
 * @param frameworkVersion framework variance version
 * @param frameworkConfig framework variance configuration
 * @param frameworkStatus framework variance status
 */
public final class StepFrameworkInstance extends AbstractStepEntity {
    private final StepEntity frameworkDefinition;
    private final String frameworkState;
    private final String frameworkVersion;
    private final List<String> frameworkConfig;
    private final String frameworkStatus;

    public StepFrameworkInstance(int id, String name, StepEntity frameworkDefinition, String frameworkState, String frameworkVersion, List<String> frameworkConfig, String frameworkStatus) {
        super(id, name);
        this.frameworkDefinition = frameworkDefinition;
        this.frameworkState = frameworkState;
        this.frameworkVersion = frameworkVersion;
        this.frameworkConfig = frameworkConfig == null ? null : java.util.List.copyOf(frameworkConfig);
        this.frameworkStatus = frameworkStatus;
    }

    public StepEntity getFrameworkDefinition() {
        return frameworkDefinition;
    }

    public String getFrameworkState() {
        return frameworkState;
    }

    public String getFrameworkVersion() {
        return frameworkVersion;
    }

    public List<String> getFrameworkConfig() {
        return frameworkConfig;
    }

    public String getFrameworkStatus() {
        return frameworkStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("frameworkDefinition", frameworkDefinition);
        state.put("frameworkState", frameworkState);
        state.put("frameworkVersion", frameworkVersion);
        state.put("frameworkConfig", frameworkConfig);
        state.put("frameworkStatus", frameworkStatus);
        return state;
    }
}
