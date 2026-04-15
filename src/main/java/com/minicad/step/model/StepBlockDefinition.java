package com.minicad.step.model;

import java.util.List;

/**
 * Resolved BLOCK_DEFINITION.
 * A block definition entity.
 *
 * @param id STEP instance id
 * @param name block name
 * @param blockType block variance type
 * @param blockSize block variance size
 * @param blockAlignment block variance alignment
 * @param blockChecksum block variance checksum type
 * @param blockStatus block variance status
 */
public record StepBlockDefinition(
    int id,
    String name,
    String blockType,
    int blockSize,
    int blockAlignment,
    String blockChecksum,
    String blockStatus) implements StepEntity {
}