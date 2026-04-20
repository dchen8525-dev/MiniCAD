package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
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
