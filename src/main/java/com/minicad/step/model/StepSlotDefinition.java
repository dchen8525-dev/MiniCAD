package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SLOT_DEFINITION.
 * A slot definition entity.
 *
 * @param id STEP instance id
 * @param name slot name
 * @param profile profile definition
 * @param depth slot depth
 * @param direction slot direction
 * @param length slot length
 * @param bottomType bottom type
 */
public record StepSlotDefinition(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction,
    Double length,
    String bottomType) implements StepEntity {
}