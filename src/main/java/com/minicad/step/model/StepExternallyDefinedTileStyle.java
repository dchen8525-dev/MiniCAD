package com.minicad.step.model;

/**
 * Resolved EXTERNALLY_DEFINED_TILE_STYLE.
 * A tile style defined by an external source.
 *
 * @param id STEP instance id
 * @param name style name
 * @param item externally defined item
 * @param source external source
 */
public record StepExternallyDefinedTileStyle(
    int id,
    String name,
    StepEntity item,
    StepEntity source) implements StepEntity {
}
