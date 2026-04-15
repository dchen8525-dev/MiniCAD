package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CUTOUT.
 * Represents a cutout feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name cutout name
 * @param profile profile definition
 * @param depth cutout depth
 * @param direction cutout direction
 */
public record StepCutout(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction) implements StepEntity {
}