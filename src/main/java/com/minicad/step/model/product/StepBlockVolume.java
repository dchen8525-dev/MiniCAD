package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved BLOCK_VOLUME.
 * A block-shaped volume defined by position and dimensions.
 *
 * @param id STEP instance id
 * @param name volume name
 * @param position axis2 placement
 * @param xLength x dimension
 * @param yLength y dimension
 * @param zLength z dimension
 */
public record StepBlockVolume(
    int id,
    String name,
    StepEntity position,
    double xLength,
    double yLength,
    double zLength) implements StepEntity {
}
