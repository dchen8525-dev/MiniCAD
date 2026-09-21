package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal shape aspect relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingShapeAspect source shape aspect (or subtype)
 * @param relatedShapeAspect target shape aspect (or subtype)
 * @param entityName concrete STEP entity name
 */
public final class StepShapeAspectRelationship extends AbstractStepEntity {
    private final String description;
    private final StepEntity relatingShapeAspect;
    private final StepEntity relatedShapeAspect;
    private final String entityName;

    public StepShapeAspectRelationship(int id, String name, String description, StepEntity relatingShapeAspect, StepEntity relatedShapeAspect, String entityName) {
        super(id, name);
        this.description = description;
        this.relatingShapeAspect = relatingShapeAspect;
        this.relatedShapeAspect = relatedShapeAspect;
        this.entityName = entityName;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getRelatingShapeAspect() {
        return relatingShapeAspect;
    }

    public StepEntity getRelatedShapeAspect() {
        return relatedShapeAspect;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public StepEntity relatingShapeAspect() {
        return relatingShapeAspect;
    }

    public StepEntity relatedShapeAspect() {
        return relatedShapeAspect;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingShapeAspect", relatingShapeAspect);
        state.put("relatedShapeAspect", relatedShapeAspect);
        state.put("entityName", entityName);
        return state;
    }
}
