package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CHAMFER_DEFINITION.
 * A chamfer definition entity.
 *
 * @param id STEP instance id
 * @param name chamfer name
 * @param edges edges being chamfered
 * @param angle chamfer angle
 * @param width chamfer width
 */
public record StepChamferDefinition(
    int id,
    String name,
    List<StepEntity> edges,
    Double angle,
    Double width) implements StepEntity {
}