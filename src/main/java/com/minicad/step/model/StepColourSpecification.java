package com.minicad.step.model;

/**
 * Minimal COLOUR_SPECIFICATION.
 *
 * @param id step id
 * @param name colour name
 */
public record StepColourSpecification(int id, String name) implements StepEntity {
}
