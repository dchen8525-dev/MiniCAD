package com.minicad.step.model;

/**
 * Minimal bounded-curve marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public record StepBoundedCurve(int id, String name) implements StepEntity {
}
