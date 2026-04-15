package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ASSEMBLY_PROCESS_PLAN.
 * An assembly process plan representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items (assembly steps)
 * @param context representation context
 * @param assemblySequence assembly sequence operations
 */
public record StepAssemblyProcessPlan(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context,
    List<StepEntity> assemblySequence) implements StepEntity {
}