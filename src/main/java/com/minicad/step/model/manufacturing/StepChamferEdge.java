package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CHAMFER_EDGE.
 * A chamfer edge entity.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param originalEdge original edge being chamfered
 * @param chamferAngle chamfer angle in degrees
 * @param chamferWidth chamfer width/distance
 * @param adjacentFaces adjacent faces for chamfer
 * @param chamferType chamfer type classification (symmetric, asymmetric)
 */
public record StepChamferEdge(
    int id,
    String name,
    StepEntity originalEdge,
    double chamferAngle,
    double chamferWidth,
    List<StepEntity> adjacentFaces,
    String chamferType) implements StepEntity {
}