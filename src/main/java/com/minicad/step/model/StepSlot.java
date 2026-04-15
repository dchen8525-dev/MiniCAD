package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SLOT.
 * Represents a slot feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name slot name
 * @param profile profile definition
 * @param depth slot depth
 * @param direction slot direction
 * @param length slot length
 */
public record StepSlot(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction,
    Double length) implements StepEntity {
}