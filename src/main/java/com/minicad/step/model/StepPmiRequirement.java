package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "description", description,
                "requirementType", requirementType);
    }
}
