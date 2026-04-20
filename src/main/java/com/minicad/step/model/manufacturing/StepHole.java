package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved HOLE.
 * Represents a hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param profile profile definition (typically circular)
 * @param depth hole depth
 * @param direction hole direction
 * @param bottomType bottom type (through, blind, etc)
 */
public record StepHole(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction,
    String bottomType) implements StepEntity {
}