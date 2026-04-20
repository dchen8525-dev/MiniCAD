package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.geometry.StepCartesianTransformationOperator;
/**
 * Minimal SOLID_REPLICA parse-only solid model.
 *
 * @param id STEP instance id
 * @param name replica name
 * @param parentSolid source solid
 * @param transformation placement transformation
 */
public record StepSolidReplica(
    int id, String name, StepEntity parentSolid, StepCartesianTransformationOperator transformation)
    implements StepEntity {
}
