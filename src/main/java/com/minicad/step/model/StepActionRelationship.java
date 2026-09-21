package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepActionRelationship extends AbstractStepEntity {
    private final String description;
    private final StepEntity relatingAction;
    private final StepEntity relatedAction;

    public StepActionRelationship(int id, String name, String description, StepEntity relatingAction, StepEntity relatedAction) {
        super(id, name);
        this.description = description;
        this.relatingAction = relatingAction;
        this.relatedAction = relatedAction;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getRelatingAction() {
        return relatingAction;
    }

    public StepEntity getRelatedAction() {
        return relatedAction;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingAction", relatingAction);
        state.put("relatedAction", relatedAction);
        return state;
    }
}
