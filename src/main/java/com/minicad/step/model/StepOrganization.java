package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal ORGANIZATION metadata.
 *
 * @param id STEP instance id
 * @param identifier organization identifier
 * @param name organization name
 * @param description organization description
 */
public final class StepOrganization extends AbstractStepEntity {
    private final String identifier;
    private final String description;

    public StepOrganization(int id, String identifier, String name, String description) {
        super(id, name);
        this.identifier = identifier;
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "identifier", identifier, "name", getName(), "description", description);
    }
}
