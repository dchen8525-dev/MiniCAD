package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved EXTERNALLY_DEFINED_TILE_STYLE.
 */
public record StepExternallyDefinedTileStyle(
    int id,
    String name,
    StepEntity externalSource) implements StepEntity {
}
