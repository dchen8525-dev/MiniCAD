package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FRAMEWORK_DEFINITION.
 * A framework definition entity.
 *
 * @param id STEP instance id
 * @param name framework name
 * @param frameworkType framework variance type
 * @param frameworkDescription framework variance description
 * @param frameworkModules framework variance module definitions
 * @param frameworkExtensions framework variance extension points
 * @param frameworkStatus framework variance status
 */
public final class StepFrameworkDefinition extends AbstractStepEntity {
    private final String frameworkType;
    private final String frameworkDescription;
    private final List<StepEntity> frameworkModules;
    private final List<String> frameworkExtensions;
    private final String frameworkStatus;

    public StepFrameworkDefinition(int id, String name, String frameworkType, String frameworkDescription, List<StepEntity> frameworkModules, List<String> frameworkExtensions, String frameworkStatus) {
        super(id, name);
        this.frameworkType = frameworkType;
        this.frameworkDescription = frameworkDescription;
        this.frameworkModules = frameworkModules == null ? null : java.util.List.copyOf(frameworkModules);
        this.frameworkExtensions = frameworkExtensions == null ? null : java.util.List.copyOf(frameworkExtensions);
        this.frameworkStatus = frameworkStatus;
    }

    public String getFrameworkType() {
        return frameworkType;
    }

    public String getFrameworkDescription() {
        return frameworkDescription;
    }

    public List<StepEntity> getFrameworkModules() {
        return frameworkModules;
    }

    public List<String> getFrameworkExtensions() {
        return frameworkExtensions;
    }

    public String getFrameworkStatus() {
        return frameworkStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("frameworkType", frameworkType);
        state.put("frameworkDescription", frameworkDescription);
        state.put("frameworkModules", frameworkModules);
        state.put("frameworkExtensions", frameworkExtensions);
        state.put("frameworkStatus", frameworkStatus);
        return state;
    }
}
