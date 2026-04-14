package com.minicad.step.model;

/**
 * Resolved CSG_VOLUME.
 * A CSG solid represented as a volume.
 *
 * @param id STEP instance id
 * @param name volume name
 * @param treeRoot root of the CSG tree
 */
public record StepCsgVolume(
    int id,
    String name,
    StepEntity treeRoot) implements StepEntity {
}
