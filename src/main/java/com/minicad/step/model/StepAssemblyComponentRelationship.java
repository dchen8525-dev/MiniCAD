package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved ASSEMBLY_COMPONENT_RELATIONSHIP.
 * Relationship between assembly components.
 */
public final class StepAssemblyComponentRelationship extends AbstractStepEntity {
    private final String description;
    private final StepEntity relatingComponent;
    private final StepEntity relatedComponent;

    public StepAssemblyComponentRelationship(int id, String name, String description, StepEntity relatingComponent, StepEntity relatedComponent) {
        super(id, name);
        this.description = description;
        this.relatingComponent = relatingComponent;
        this.relatedComponent = relatedComponent;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getRelatingComponent() {
        return relatingComponent;
    }

    public StepEntity getRelatedComponent() {
        return relatedComponent;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description,
                "relatingComponent", relatingComponent, "relatedComponent", relatedComponent);
    }
}
