package com.minicad.step.model;

/**
 * Minimal DRAUGHTING_PRE_DEFINED_COLOUR.
 *
 * @param id step id
 * @param name predefined colour name
 */
public record StepDraughtingPreDefinedColour(int id, String name) implements StepEntity {
}
