package com.minicad.step.model;

/**
 * Minimal LANGUAGE metadata.
 *
 * @param id STEP instance id
 * @param name language name
 */
public record StepLanguage(
        int id,
        String name
) implements StepEntity {
}
