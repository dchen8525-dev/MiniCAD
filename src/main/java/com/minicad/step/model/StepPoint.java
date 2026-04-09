package com.minicad.step.model;

/**
 * Minimal point marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public record StepPoint(int id, String name) implements StepEntity {
}
