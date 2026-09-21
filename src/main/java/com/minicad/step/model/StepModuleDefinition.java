package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MODULE_DEFINITION.
 * A module definition entity.
 *
 * @param id STEP instance id
 * @param name module name
 * @param moduleType module variance type
 * @param moduleDescription module variance description
 * @param moduleComponents module variance components
 * @param moduleInterfaces module variance interfaces
 * @param moduleStatus module variance status
 */
public final class StepModuleDefinition extends AbstractStepEntity {
    private final String moduleType;
    private final String moduleDescription;
    private final List<StepEntity> moduleComponents;
    private final List<StepEntity> moduleInterfaces;
    private final String moduleStatus;

    public StepModuleDefinition(int id, String name, String moduleType, String moduleDescription, List<StepEntity> moduleComponents, List<StepEntity> moduleInterfaces, String moduleStatus) {
        super(id, name);
        this.moduleType = moduleType;
        this.moduleDescription = moduleDescription;
        this.moduleComponents = moduleComponents == null ? null : java.util.List.copyOf(moduleComponents);
        this.moduleInterfaces = moduleInterfaces == null ? null : java.util.List.copyOf(moduleInterfaces);
        this.moduleStatus = moduleStatus;
    }

    public String getModuleType() {
        return moduleType;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public List<StepEntity> getModuleComponents() {
        return moduleComponents;
    }

    public List<StepEntity> getModuleInterfaces() {
        return moduleInterfaces;
    }

    public String getModuleStatus() {
        return moduleStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("moduleType", moduleType);
        state.put("moduleDescription", moduleDescription);
        state.put("moduleComponents", moduleComponents);
        state.put("moduleInterfaces", moduleInterfaces);
        state.put("moduleStatus", moduleStatus);
        return state;
    }
}
