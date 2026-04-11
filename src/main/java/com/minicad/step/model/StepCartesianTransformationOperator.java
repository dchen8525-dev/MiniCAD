package com.minicad.step.model;

/**
 * Minimal CARTESIAN_TRANSFORMATION_OPERATOR_2D/3D.
 *
 * @param id step id
 * @param name step label
 * @param axis1 optional first axis
 * @param axis2 optional second axis
 * @param localOrigin local origin point
 * @param scale optional scale factor
 * @param axis3 optional third axis for 3D operators
 * @param entityName concrete STEP entity name
 */
public record StepCartesianTransformationOperator(
    int id,
    String name,
    StepDirection axis1,
    StepDirection axis2,
    StepCartesianPoint localOrigin,
    Double scale,
    StepDirection axis3,
    String entityName)
    implements StepEntity {}
