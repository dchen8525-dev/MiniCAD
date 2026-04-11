package com.minicad.step.model;

/**
 * Minimal LANGUAGE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedLanguage assigned language
 */
public record StepLanguageAssignment(
        int id,
        StepLanguage assignedLanguage
) implements StepEntity {

    @Override
    public String name() {
        return assignedLanguage.name();
    }
}
