package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal ORGANIZATION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingOrganization source organization
 * @param relatedOrganization target organization
 */
public final class StepOrganizationRelationship extends AbstractStepEntity {
    private final String description;
    private final StepOrganization relatingOrganization;
    private final StepOrganization relatedOrganization;

    public StepOrganizationRelationship(int id, String name, String description, StepOrganization relatingOrganization, StepOrganization relatedOrganization) {
        super(id, name);
        this.description = description;
        this.relatingOrganization = relatingOrganization;
        this.relatedOrganization = relatedOrganization;
    }

    public String getDescription() {
        return description;
    }

    public StepOrganization getRelatingOrganization() {
        return relatingOrganization;
    }

    public StepOrganization getRelatedOrganization() {
        return relatedOrganization;
    }

    // Record-style accessors
    public StepOrganization relatingOrganization() {
        return relatingOrganization;
    }

    public StepOrganization relatedOrganization() {
        return relatedOrganization;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingOrganization", relatingOrganization);
        state.put("relatedOrganization", relatedOrganization);
        return state;
    }
}
