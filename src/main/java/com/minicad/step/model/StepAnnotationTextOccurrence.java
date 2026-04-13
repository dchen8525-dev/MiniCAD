package com.minicad.step.model;

/**
 * Minimal annotation text occurrence for presentation PMI.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param text annotation text
 * @param position anchor point
 */
public record StepAnnotationTextOccurrence(
        int id,
        String name,
        String text,
        StepEntity position
) implements StepEntity {
}
