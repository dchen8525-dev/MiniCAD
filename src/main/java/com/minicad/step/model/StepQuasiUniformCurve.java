package com.minicad.step.model;

/**
 * Minimal quasi-uniform-curve marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public record StepQuasiUniformCurve(int id, String name) implements StepEntity {
}
