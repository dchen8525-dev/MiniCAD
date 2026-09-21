package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved PMI_REQUIREMENT.
 */
public final class StepPmiRequirement extends AbstractStepEntity {
    private final String description;
    private final String requirementType;

    public StepPmiRequirement(int id, String name, String description, String requirementType) {
        super(id, name);
        this.description = description;
        this.requirementType = requirementType;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirementType() {
        return requirementType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("requirementType", requirementType);
        return state;
    }
}
