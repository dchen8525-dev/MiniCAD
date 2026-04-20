package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MACHINING_TOOLPATH.
 * A machining toolpath entity.
 *
 * @param id STEP instance id
 * @param name toolpath name
 * @param pathGeometry path geometry curve/curve set
 * @param tool tool used for this path
 * @param pathParameters path parameters (speed, feed, etc.)
 * @param approachStrategy approach strategy configuration
 */
public record StepMachiningToolpath(
    int id,
    String name,
    StepEntity pathGeometry,
    StepEntity tool,
    List<StepEntity> pathParameters,
    StepEntity approachStrategy) implements StepEntity {
}