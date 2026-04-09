package com.minicad.step.model;

/**
 * Minimal uniform-surface marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public record StepUniformSurface(int id, String name) implements StepEntity {
}
