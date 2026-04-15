package com.minicad.step.model;

import java.util.List;

/**
 * Resolved RIB_DEFINITION.
 * A rib definition entity.
 *
 * @param id STEP instance id
 * @param name rib name
 * @param profile profile definition
 * @param height rib height
 * @param direction rib direction
 * @param taperAngle optional taper angle
 */
public record StepRibDefinition(
    int id,
    String name,
    StepEntity profile,
    Double height,
    StepEntity direction,
    Double taperAngle) implements StepEntity {
}