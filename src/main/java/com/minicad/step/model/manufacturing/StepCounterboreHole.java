package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COUNTERBORE_HOLE.
 * Represents a counterbore hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name counterbore name
 * @param throughHole through hole reference
 * @param counterboreDiameter counterbore diameter
 * @param counterboreDepth counterbore depth
 */
public record StepCounterboreHole(
    int id,
    String name,
    StepEntity throughHole,
    Double counterboreDiameter,
    Double counterboreDepth) implements StepEntity {
}