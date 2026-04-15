package com.minicad.step.model;

/**
 * Resolved BLOCK_INSTANCE.
 * A block instance entity.
 *
 * @param id STEP instance id
 * @param name block instance name
 * @param blockDefinition block variance definition reference
 * @param blockAddress block variance address
 * @param blockUsed block variance used size
 * @param blockStatus block variance status
 */
public record StepBlockInstance(
    int id,
    String name,
    StepEntity blockDefinition,
    long blockAddress,
    int blockUsed,
    String blockStatus) implements StepEntity {
}