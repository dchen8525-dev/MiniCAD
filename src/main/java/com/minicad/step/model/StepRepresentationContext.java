package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal semantic representation context.
 *
 * @param id STEP instance id
 * @param contextIdentifier context identifier
 * @param contextType context type
 */
public final class StepRepresentationContext extends AbstractStepEntity {
    private final String contextIdentifier;
    private final String contextType;

    public StepRepresentationContext(int id, String contextIdentifier, String contextType) {
        super(id, "");
        this.contextIdentifier = contextIdentifier;
        this.contextType = contextType;
    }

    public String getContextIdentifier() {
        return contextIdentifier;
    }

    public String getContextType() {
        return contextType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("contextIdentifier", contextIdentifier);
        state.put("contextType", contextType);
        return state;
    }
}
