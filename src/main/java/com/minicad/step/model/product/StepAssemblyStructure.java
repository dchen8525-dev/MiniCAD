package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ASSEMBLY_STRUCTURE.
 * An assembly structure entity.
 *
 * @param id STEP instance id
 * @param name assembly name
 * @param rootComponent root component of assembly
 * @param components list of assembly components
 * @param relationships component relationships
 * @param assemblyType assembly type classification
 */
public record StepAssemblyStructure(
    int id,
    String name,
    StepEntity rootComponent,
    List<StepEntity> components,
    List<StepEntity> relationships,
    String assemblyType) implements StepEntity {
}