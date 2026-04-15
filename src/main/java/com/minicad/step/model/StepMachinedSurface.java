package com.minicad.step.model;

/**
 * Resolved MACHINED_SURFACE.
 * Represents a surface that has been machined.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param face the face that was machined
 */
public record StepMachinedSurface(
    int id,
    String name,
    StepEntity face) implements StepEntity {
}