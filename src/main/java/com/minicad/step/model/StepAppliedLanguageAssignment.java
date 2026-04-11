package com.minicad.step.model;

import java.util.List;

/**
 * Minimal APPLIED_LANGUAGE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedLanguage assigned language
 * @param items assigned target items
 */
public record StepAppliedLanguageAssignment(
        int id,
        StepLanguage assignedLanguage,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedLanguageAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return assignedLanguage.name();
    }
}
