package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ROUND.
 * Represents a round/fillet feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name round name
 * @param edges edges being rounded
 * @param radius fillet radius
 */
public record StepRound(
    int id,
    String name,
    List<StepEntity> edges,
    Double radius) implements StepEntity {
}