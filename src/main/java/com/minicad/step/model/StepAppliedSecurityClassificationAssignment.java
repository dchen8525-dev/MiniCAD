package com.minicad.step.model;

import java.util.List;

/**
 * Minimal APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedSecurityClassification assigned security classification
 * @param items assigned target items
 */
public record StepAppliedSecurityClassificationAssignment(
        int id,
        String entityName,
        StepSecurityClassification assignedSecurityClassification,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedSecurityClassificationAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return assignedSecurityClassification.name();
    }
}
