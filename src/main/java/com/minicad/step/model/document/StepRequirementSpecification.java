package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved REQUIREMENT_SPECIFICATION.
 * A requirement specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param specificationId specification identifier
 * @param requirements list of requirements
 * @param specificationType specification type (functional, performance, safety)
 * @param specificationStatus specification status
 * @param specificationVersion specification version reference
 */
public record StepRequirementSpecification(
    int id,
    String name,
    String specificationId,
    List<StepEntity> requirements,
    String specificationType,
    String specificationStatus,
    StepEntity specificationVersion) implements StepEntity {
}