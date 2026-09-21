package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TEXT_LITERAL.
 * A single text string at a specific placement.
 *
 * @param id STEP instance id
 * @param name text name
 * @param literal the text content
 * @param path text path placement
 */
public final class StepTextLiteral extends AbstractStepEntity {
    private final String literal;
    private final StepEntity path;

    public StepTextLiteral(int id, String name, String literal, StepEntity path) {
        super(id, name);
        this.literal = literal;
        this.path = path;
    }

    public String getLiteral() {
        return literal;
    }

    public StepEntity getPath() {
        return path;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("literal", literal);
        state.put("path", path);
        return state;
    }
}
