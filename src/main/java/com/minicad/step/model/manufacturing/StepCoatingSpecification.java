package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COATING_SPECIFICATION.
 * A coating specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param specificationType specification variance type
 * @param specificationDescription specification variance description
 * @param specificationRequirements specification variance requirements
 * @param specificationStatus specification variance status
 */
public record StepCoatingSpecification(
    int id,
    String name,
    String specificationType,
    String specificationDescription,
    List<String> specificationRequirements,
    String specificationStatus) implements StepEntity {
}