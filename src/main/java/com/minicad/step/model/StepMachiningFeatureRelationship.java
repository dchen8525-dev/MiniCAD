package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MACHINING_FEATURE_RELATIONSHIP.
 * A machining feature relationship entity.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param relatingFeature relating machining feature
 * @param relatedFeature related machining feature
 * @param relationshipType relationship type classification
 * @param description relationship description
 */
public final class StepMachiningFeatureRelationship extends AbstractStepEntity {
    private final StepEntity relatingFeature;
    private final StepEntity relatedFeature;
    private final String relationshipType;
    private final String description;

    public StepMachiningFeatureRelationship(int id, String name, StepEntity relatingFeature, StepEntity relatedFeature, String relationshipType, String description) {
        super(id, name);
        this.relatingFeature = relatingFeature;
        this.relatedFeature = relatedFeature;
        this.relationshipType = relationshipType;
        this.description = description;
    }

    public StepEntity getRelatingFeature() {
        return relatingFeature;
    }

    public StepEntity getRelatedFeature() {
        return relatedFeature;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public String getDescription() {
        return description;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("relatingFeature", relatingFeature);
        state.put("relatedFeature", relatedFeature);
        state.put("relationshipType", relationshipType);
        state.put("description", description);
        return state;
    }
}
