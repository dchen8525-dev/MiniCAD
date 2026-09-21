package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal product definition root.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param name product name
 * @param description optional description
 * @param frameOfReference product contexts
 */
public final class StepProduct extends AbstractStepEntity {
    private final String identifier;
    private final String description;
    private final List<StepProductContext> frameOfReference;

    public StepProduct(int id, String identifier, String name, String description, List<StepProductContext> frameOfReference) {
        super(id, name);
        this.identifier = identifier;
        this.description = description;
        this.frameOfReference = frameOfReference == null ? null : java.util.List.copyOf(frameOfReference);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public List<StepProductContext> getFrameOfReference() {
        return frameOfReference;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String identifier() { return identifier; }
    public String description() { return description; }
    public List<StepProductContext> frameOfReference() { return frameOfReference; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("identifier", identifier);
        state.put("name", getName());
        state.put("description", description);
        state.put("frameOfReference", frameOfReference);
        return state;
    }
}
