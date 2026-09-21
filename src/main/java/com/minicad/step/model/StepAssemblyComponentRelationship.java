package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingComponent", relatingComponent);
        state.put("relatedComponent", relatedComponent);
        return state;
    }
}
