package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal LANGUAGE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedLanguage assigned language
 */
public final class StepLanguageAssignment extends AbstractStepEntity {
    private final StepLanguage assignedLanguage;

    public StepLanguageAssignment(int id, StepLanguage assignedLanguage) {
        super(id, "");
        this.assignedLanguage = assignedLanguage;
    }

    public StepLanguage getAssignedLanguage() {
        return assignedLanguage;
    }

    // Record-style accessor
    public StepLanguage assignedLanguage() {
        return assignedLanguage;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "assignedLanguage", assignedLanguage);
    }
}
