package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved MAKE_FROM_BUILD_ASSEMBLY.
 * Manufacturing assembly definition.
 */
public final class StepMakeFromBuildAssembly extends AbstractStepEntity {
    private final String description;
    private final StepEntity assembly;

    public StepMakeFromBuildAssembly(int id, String name, String description, StepEntity assembly) {
        super(id, name);
        this.description = description;
        this.assembly = assembly;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getAssembly() {
        return assembly;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("assembly", assembly);
        return state;
    }
}
