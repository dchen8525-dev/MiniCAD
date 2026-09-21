package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal product definition.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param description optional description
 * @param formation referenced formation
 * @param frameOfReference referenced definition context
 */
public final class StepProductDefinition extends AbstractStepEntity {
    private final String identifier;
    private final String description;
    private final StepProductDefinitionFormation formation;
    private final StepProductDefinitionContext frameOfReference;

    public StepProductDefinition(int id, String identifier, String description, StepProductDefinitionFormation formation, StepProductDefinitionContext frameOfReference) {
        super(id, "");
        this.identifier = identifier;
        this.description = description;
        this.formation = formation;
        this.frameOfReference = frameOfReference;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public StepProductDefinitionFormation getFormation() {
        return formation;
    }

    public StepProductDefinitionContext getFrameOfReference() {
        return frameOfReference;
    }

    public String getName() {
        return identifier;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String identifier() { return identifier; }
    public String description() { return description; }
    public StepProductDefinitionFormation formation() { return formation; }
    public StepProductDefinitionContext frameOfReference() { return frameOfReference; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("identifier", identifier);
        state.put("description", description);
        state.put("formation", formation);
        state.put("frameOfReference", frameOfReference);
        return state;
    }
}
