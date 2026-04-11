package com.minicad.step.model;

/**
 * Minimal CSG_SOLID parse-only solid model.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param treeRootExpression CSG tree root expression
 */
public record StepCsgSolid(int id, String name, StepEntity treeRootExpression)
    implements StepEntity {
}
