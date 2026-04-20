package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PROJECTED_ZONE_DEFINITION.
 * A projected tolerance zone definition entity.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param projectionLength projection length
 * @param projectionDirection projection direction
 * @param projectionUnit projection unit
 */
public record StepProjectedZoneDefinition(
    int id,
    String name,
    Double projectionLength,
    StepEntity projectionDirection,
    StepEntity projectionUnit) implements StepEntity {
}