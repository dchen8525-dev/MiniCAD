package com.minicad.step.model;

/**
 * Minimal uniform-curve marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public record StepUniformCurve(int id, String name) implements StepEntity {
}
