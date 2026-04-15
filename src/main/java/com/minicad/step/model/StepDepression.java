package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DEPRESSION.
 * Represents a depression/pocket feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name depression name
 * @param profile profile definition
 * @param depth depression depth
 * @param direction depression direction
 * @param taperAngle optional taper angle
 */
public record StepDepression(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction,
    Double taperAngle) implements StepEntity {
}