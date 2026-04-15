package com.minicad.step.model;

import java.util.List;

/**
 * Resolved RIB.
 * Represents a rib feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name rib name
 * @param profile profile definition
 * @param height rib height
 * @param direction rib direction
 */
public record StepRib(
    int id,
    String name,
    StepEntity profile,
    Double height,
    StepEntity direction) implements StepEntity {
}