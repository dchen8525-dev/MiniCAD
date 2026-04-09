package com.minicad.step.model;

/**
 * Minimal representation item marker.
 *
 * @param id STEP instance id
 * @param name item name
 */
public record StepRepresentationItem(int id, String name) implements StepEntity {
}
