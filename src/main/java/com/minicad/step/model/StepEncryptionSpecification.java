package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ENCRYPTION_SPECIFICATION.
 * An encryption specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceAlgorithm encryption variance algorithm
 * @varianceKeySize key variance size
 * @varianceMode encryption variance mode
 * @varianceKeyManagement key variance management specification
 * @varianceStatus specification variance status
 */
public record StepEncryptionSpecification(
    int id,
    String name,
    String varianceAlgorithm,
    int varianceKeySize,
    String varianceMode,
    StepEntity varianceKeyManagement,
    String varianceStatus) implements StepEntity {
}