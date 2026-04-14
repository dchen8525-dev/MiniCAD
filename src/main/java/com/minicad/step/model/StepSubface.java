package com.minicad.step.model;

/**
 * Resolved SUBFACE.
 * A sub-face of a connected face set.
 *
 * @param id STEP instance id
 * @param name subface name
 * @param faceElement the underlying face entity
 */
public record StepSubface(int id, String name, StepEntity faceElement) implements StepEntity {
}
