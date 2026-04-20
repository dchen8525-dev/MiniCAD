package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CHAMFER.
 * Represents a chamfer feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name chamfer name
 * @param edges edges being chamfered
 * @param angle chamfer angle (optional)
 * @param width chamfer width (optional)
 */
public record StepChamfer(
    int id,
    String name,
    List<StepEntity> edges,
    Double angle,
    Double width) implements StepEntity {
}