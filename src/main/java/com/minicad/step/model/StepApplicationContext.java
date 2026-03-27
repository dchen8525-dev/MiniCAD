package com.minicad.step.model;

/**
 * Minimal application context.
 *
 * @param id STEP instance id
 * @param application application domain text
 */
public record StepApplicationContext(int id, String application) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
