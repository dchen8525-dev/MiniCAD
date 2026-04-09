package com.minicad.step.model;

/**
 * Minimal bezier-curve marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public record StepBezierCurve(int id, String name) implements StepEntity {
}
