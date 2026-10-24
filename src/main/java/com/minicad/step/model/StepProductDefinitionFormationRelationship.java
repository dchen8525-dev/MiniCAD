package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal PRODUCT_DEFINITION_FORMATION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param identifier relationship identifier
 * @param name relationship name
 * @param description relationship description
 * @param relatingFormation source formation
 * @param relatedFormation target formation
 */
public final class StepProductDefinitionFormationRelationship extends AbstractStepEntity {
    private final String identifier;
    private final String description;
    private final StepProductDefinitionFormation relatingFormation;
    private final StepProductDefinitionFormation relatedFormation;

    public StepProductDefinitionFormationRelationship(int id, String identifier, String name, String description, StepProductDefinitionFormation relatingFormation, StepProductDefinitionFormation relatedFormation) {
        super(id, name);
        this.identifier = identifier;
        this.description = description;
        this.relatingFormation = relatingFormation;
        this.relatedFormation = relatedFormation;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public StepProductDefinitionFormation getRelatingFormation() {
        return relatingFormation;
    }

    public StepProductDefinitionFormation getRelatedFormation() {
        return relatedFormation;
    }

    // Record-style accessors
    public StepProductDefinitionFormation relatingFormation() {
        return relatingFormation;
    }

    public StepProductDefinitionFormation relatedFormation() {
        return relatedFormation;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "identifier", identifier, "name", getName(), "description", description,
                "relatingFormation", relatingFormation, "relatedFormation", relatedFormation);
    }
}
