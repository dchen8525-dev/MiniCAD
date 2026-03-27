package com.minicad.step.model;

/**
 * Minimal fill area style colour.
 *
 * @param id STEP instance id
 * @param name style name
 * @param colour referenced colour
 */
public record StepFillAreaStyleColour(int id, String name, StepColourRgb colour) implements StepEntity {
}
