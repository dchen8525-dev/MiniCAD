package com.minicad.step.model;

import java.util.List;

/**
 * Resolved STEP.
 * Represents a step feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name step name
 * @param profile profile definition
 * @param depth step depth
 * @param direction step direction
 */
public record StepStep(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction) implements StepEntity {
}