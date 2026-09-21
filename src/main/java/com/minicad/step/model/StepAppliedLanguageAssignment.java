package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_LANGUAGE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedLanguage assigned language
 * @param items assigned target items
 */
public final class StepAppliedLanguageAssignment extends AbstractStepEntity {
    private final StepLanguage assignedLanguage;
    private final List<StepEntity> items;

    public StepAppliedLanguageAssignment(int id, StepLanguage assignedLanguage, List<StepEntity> items) {
        super(id, "");
        this.assignedLanguage = assignedLanguage;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public StepLanguage getAssignedLanguage() {
        return assignedLanguage;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepLanguage assignedLanguage() {
        return assignedLanguage;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedLanguage", assignedLanguage);
        state.put("items", items);
        return state;
    }
}
