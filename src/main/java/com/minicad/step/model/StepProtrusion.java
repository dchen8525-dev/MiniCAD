package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PROTRUSION.
 * Represents a protrusion feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name protrusion name
 * @param profile profile definition
 * @param height protrusion height
 * @param direction protrusion direction
 * @param taperAngle optional taper angle
 */
public record StepProtrusion(
    int id,
    String name,
    StepEntity profile,
    Double height,
    StepEntity direction,
    Double taperAngle) implements StepEntity {
}