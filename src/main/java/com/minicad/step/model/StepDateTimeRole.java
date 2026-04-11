package com.minicad.step.model;

/**
 * Minimal DATE_TIME_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public record StepDateTimeRole(
        int id,
        String name
) implements StepEntity {
}
