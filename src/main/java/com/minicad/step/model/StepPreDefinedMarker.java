package com.minicad.step.model;

/**
 * Minimal PRE_DEFINED_MARKER.
 *
 * @param id step id
 * @param name predefined marker name
 */
public record StepPreDefinedMarker(int id, String name) implements StepEntity {
}
