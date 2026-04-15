package com.minicad.step.model;

import java.util.List;

/**
 * Resolved BRANCH_DEFINITION.
 * A branch definition entity.
 *
 * @param id STEP instance id
 * @param name branch name
 * @param branchType branch variance type
 * @param branchCondition branch variance condition
 * @param branchTrue branch variance true path reference
 * @param branchFalse branch variance false path reference
 * @param branchStatus branch variance status
 */
public record StepBranchDefinition(
    int id,
    String name,
    String branchType,
    String branchCondition,
    StepEntity branchTrue,
    StepEntity branchFalse,
    String branchStatus) implements StepEntity {
}