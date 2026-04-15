package com.minicad.step.model;

/**
 * Resolved BRANCH_INSTANCE.
 * A branch instance entity.
 *
 * @param id STEP instance id
 * @param name branch instance name
 * @param branchDefinition branch variance definition reference
 * @param branchState branch variance state
 * @param branchResult branch variance result (true/false)
 * @param branchTakenPath branch variance taken path reference
 * @param branchStatus branch variance status
 */
public record StepBranchInstance(
    int id,
    String name,
    StepEntity branchDefinition,
    String branchState,
    boolean branchResult,
    StepEntity branchTakenPath,
    String branchStatus) implements StepEntity {
}