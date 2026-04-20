package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ROUND_HOLE_DEFINITION.
 * A round hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param diameter hole diameter
 * @param depth hole depth
 * @param bottomType bottom type (through, blind, flat, etc)
 */
public record StepRoundHoleDefinition(
    int id,
    String name,
    Double diameter,
    Double depth,
    String bottomType) implements StepEntity {
}