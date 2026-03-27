package com.minicad.step.model;

/**
 * Minimal product context.
 *
 * @param id STEP instance id
 * @param name context name
 * @param disciplineType discipline type
 * @param frameOfReference referenced application context
 */
public record StepProductContext(
        int id,
        String name,
        String disciplineType,
        StepApplicationContext frameOfReference
) implements StepEntity {

    @Override
    public String name() {
        return name;
    }
}
