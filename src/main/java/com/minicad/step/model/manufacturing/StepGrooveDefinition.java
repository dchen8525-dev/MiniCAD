package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved GROOVE_DEFINITION.
 * A groove definition entity.
 *
 * @param id STEP instance id
 * @param name groove name
 * @param profile profile definition
 * @param depth groove depth
 * @param direction groove direction
 * @param grooveType groove type
 */
public record StepGrooveDefinition(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction,
    String grooveType) implements StepEntity {
}