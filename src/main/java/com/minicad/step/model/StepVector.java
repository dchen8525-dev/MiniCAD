package com.minicad.step.model;

/**
 * Resolved VECTOR.
 *
 * @param id step id
 * @param name step label
 * @param orientation referenced direction
 * @param magnitude vector magnitude
 */
public record StepVector(int id, String name, StepDirection orientation, double magnitude) implements StepEntity {
}
