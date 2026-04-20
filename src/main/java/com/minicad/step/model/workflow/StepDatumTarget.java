package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved DATUM_TARGET.
 * A datum target used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name target name
 * @param targetId target identifier
 * @param targetShape target shape reference
 */
public record StepDatumTarget(
    int id,
    String name,
    String targetId,
    StepEntity targetShape) implements StepEntity {
}
