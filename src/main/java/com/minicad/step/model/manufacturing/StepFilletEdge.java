package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FILLET_EDGE.
 * A fillet edge entity.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param originalEdge original edge being filleted
 * @param filletRadius fillet radius
 * @param adjacentFaces adjacent faces for fillet
 * @param filletType fillet type classification (constant, variable)
 */
public record StepFilletEdge(
    int id,
    String name,
    StepEntity originalEdge,
    double filletRadius,
    List<StepEntity> adjacentFaces,
    String filletType) implements StepEntity {
}