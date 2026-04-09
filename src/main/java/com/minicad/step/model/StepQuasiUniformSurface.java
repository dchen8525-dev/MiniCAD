package com.minicad.step.model;

/**
 * Minimal quasi-uniform-surface marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public record StepQuasiUniformSurface(int id, String name) implements StepEntity {
}
