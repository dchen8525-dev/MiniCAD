package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved POCKET_DEFINITION.
 * A pocket definition entity.
 *
 * @param id STEP instance id
 * @param name pocket name
 * @param profile profile definition
 * @param depth pocket depth
 * @param direction pocket direction
 * @param floorType floor type
 */
public record StepPocketDefinition(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction,
    String floorType) implements StepEntity {
}