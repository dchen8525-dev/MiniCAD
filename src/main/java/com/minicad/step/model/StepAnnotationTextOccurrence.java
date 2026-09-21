package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal annotation text occurrence for presentation PMI.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param text annotation text
 * @param position anchor point
 */
public final class StepAnnotationTextOccurrence extends AbstractStepEntity {
    private final String text;
    private final StepEntity position;

    public StepAnnotationTextOccurrence(int id, String name, String text, StepEntity position) {
        super(id, name);
        this.text = text;
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public StepEntity getPosition() {
        return position;
    }

    // Record-style accessors
    public String text() {
        return text;
    }

    public StepEntity position() {
        return position;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("text", text);
        state.put("position", position);
        return state;
    }
}
