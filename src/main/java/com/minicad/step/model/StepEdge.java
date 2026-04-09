package com.minicad.step.model;

/**
 * Minimal edge marker.
 *
 * @param id STEP instance id
 * @param name inherited topological-representation-item name when available
 */
public record StepEdge(int id, String name) implements StepEntity {
}
