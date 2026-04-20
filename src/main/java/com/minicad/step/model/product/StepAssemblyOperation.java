package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ASSEMBLY_OPERATION.
 * An assembly operation entity.
 *
 * @param id STEP instance id
 * @param name operation name
 * @param operationType assembly operation type (fasten, insert, align, weld)
 * @param operationParameters operation parameters
 * @param components components involved in operation
 * @param toolRequirement tool requirement reference
 * @param fixtureRequirement fixture requirement reference
 * @param operationTime estimated operation time
 */
public record StepAssemblyOperation(
    int id,
    String name,
    String operationType,
    List<Double> operationParameters,
    List<StepEntity> components,
    StepEntity toolRequirement,
    StepEntity fixtureRequirement,
    double operationTime) implements StepEntity {
}