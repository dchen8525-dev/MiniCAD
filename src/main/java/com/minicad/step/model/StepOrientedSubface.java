package com.minicad.step.model;

/**
 * Resolved ORIENTED_SUBFACE.
 * An oriented reference to a sub-face.
 *
 * @param id STEP instance id
 * @param name subface name
 * @param faceElement the underlying subface entity
 * @param orientation orientation flag
 */
public record StepOrientedSubface(int id, String name, StepEntity faceElement, boolean orientation) implements StepEntity {
}
