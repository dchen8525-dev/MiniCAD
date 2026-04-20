package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved GROOVE.
 * Represents a groove feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name groove name
 * @param profile profile definition
 * @param depth groove depth
 * @param direction groove direction
 */
public record StepGroove(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction) implements StepEntity {
}