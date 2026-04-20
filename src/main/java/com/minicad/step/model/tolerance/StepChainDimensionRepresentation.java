package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CHAIN_DIMENSION_REPRESENTATION.
 * A chain dimension representation entity.
 *
 * @param id STEP instance id
 * * @param name representation name
 * @param items representation items (chain of dimensions)
 * * @param context representation context
 * @param chainOrigin chain origin point
 */
public record StepChainDimensionRepresentation(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context,
    StepEntity chainOrigin) implements StepEntity {
}