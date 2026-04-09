package com.minicad.step.model;

/**
 * Minimal topological representation item marker.
 *
 * @param id STEP instance id
 * @param name item name
 */
public record StepTopologicalRepresentationItem(int id, String name) implements StepEntity {
}
