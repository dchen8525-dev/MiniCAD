package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal draughting callout containing PMI text and optional leader geometry.
 *
 * @param id STEP instance id
 * @param name callout name
 * @param contents callout contents
 * @param entityName concrete STEP entity name
 */
public final class StepDraughtingCallout extends AbstractStepEntity {
    private final List<StepEntity> contents;
    private final String entityName;

    public StepDraughtingCallout(int id, String name, List<StepEntity> contents, String entityName) {
        super(id, name);
        this.contents = contents == null ? null : java.util.List.copyOf(contents);
        this.entityName = entityName;
    }

    public List<StepEntity> getContents() {
        return contents;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public List<StepEntity> contents() {
        return contents;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("contents", contents);
        state.put("entityName", entityName);
        return state;
    }
}
