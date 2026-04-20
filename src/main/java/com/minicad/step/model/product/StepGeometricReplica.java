package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.geometry.StepCartesianTransformationOperator;
/**
 * Minimal parse-only POINT_REPLICA, CURVE_REPLICA or SURFACE_REPLICA.
 *
 * @param id STEP instance id
 * @param name replica name
 * @param parent replicated geometric item
 * @param transformation transformation operator
 * @param entityName concrete STEP entity name
 */
public record StepGeometricReplica(
        int id,
        String name,
        StepEntity parent,
        StepCartesianTransformationOperator transformation,
        String entityName
) implements StepEntity {
}
