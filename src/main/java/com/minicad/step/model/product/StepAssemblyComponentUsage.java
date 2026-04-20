package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ASSEMBLY_COMPONENT_USAGE.
 * An assembly component usage entity.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param parentAssembly parent assembly reference
 * @param childComponent child component reference
 * @param quantity quantity of components
 * @param usageType usage type classification
 * @param location placement location
 */
public record StepAssemblyComponentUsage(
    int id,
    String name,
    StepEntity parentAssembly,
    StepEntity childComponent,
    int quantity,
    String usageType,
    StepEntity location) implements StepEntity {
}