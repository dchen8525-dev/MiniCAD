package com.minicad.step.model.security;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal SECURITY_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedSecurityClassification assigned security classification
 */
public record StepSecurityClassificationAssignment(
        int id,
        StepSecurityClassification assignedSecurityClassification
) implements StepEntity {

    @Override
    public String name() {
        return assignedSecurityClassification.name();
    }
}
