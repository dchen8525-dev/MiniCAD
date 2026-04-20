package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved OPERATOR_QUALIFICATION.
 * An operator qualification entity.
 *
 * @param id STEP instance id
 * @param name qualification name
 * @param operatorId operator identifier
 * @param qualificationType qualification type (skill, certification, training)
 * @varianceLevel qualification variance level
 * @param qualificationDate qualification date
 * @param expirationDate expiration date
 * @varianceOperations qualified variance operations
 * @param qualificationStatus qualification status
 */
public record StepOperatorQualification(
    int id,
    String name,
    String operatorId,
    String qualificationType,
    int varianceLevel,
    StepEntity qualificationDate,
    StepEntity expirationDate,
    List<StepEntity> varianceOperations,
    String qualificationStatus) implements StepEntity {
}