package com.minicad.step.model;

/**
 * Minimal bezier-surface marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public record StepBezierSurface(int id, String name) implements StepEntity {
}
