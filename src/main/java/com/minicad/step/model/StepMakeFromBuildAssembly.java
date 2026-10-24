package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "assembly", assembly);
    }
}
