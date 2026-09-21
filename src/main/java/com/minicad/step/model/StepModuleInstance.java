package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MODULE_INSTANCE.
 * A module instance entity.
 *
 * @param id STEP instance id
 * @param name module instance name
 * @param moduleDefinition module variance definition reference
 * @param moduleState module variance state
 * @param moduleVersion module variance version
 * @param moduleConfig module variance configuration
 * @param moduleStatus module variance status
 */
public final class StepModuleInstance extends AbstractStepEntity {
    private final StepEntity moduleDefinition;
    private final String moduleState;
    private final String moduleVersion;
    private final List<String> moduleConfig;
    private final String moduleStatus;

    public StepModuleInstance(int id, String name, StepEntity moduleDefinition, String moduleState, String moduleVersion, List<String> moduleConfig, String moduleStatus) {
        super(id, name);
        this.moduleDefinition = moduleDefinition;
        this.moduleState = moduleState;
        this.moduleVersion = moduleVersion;
        this.moduleConfig = moduleConfig == null ? null : java.util.List.copyOf(moduleConfig);
        this.moduleStatus = moduleStatus;
    }

    public StepEntity getModuleDefinition() {
        return moduleDefinition;
    }

    public String getModuleState() {
        return moduleState;
    }

    public String getModuleVersion() {
        return moduleVersion;
    }

    public List<String> getModuleConfig() {
        return moduleConfig;
    }

    public String getModuleStatus() {
        return moduleStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("moduleDefinition", moduleDefinition);
        state.put("moduleState", moduleState);
        state.put("moduleVersion", moduleVersion);
        state.put("moduleConfig", moduleConfig);
        state.put("moduleStatus", moduleStatus);
        return state;
    }
}
