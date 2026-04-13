package com.minicad.step.model;

import java.util.List;

/**
 * Minimal APPLIED_CERTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedCertification assigned certification
 * @param items assigned target items
 */
public record StepAppliedCertificationAssignment(
        int id,
        String entityName,
        StepCertification assignedCertification,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedCertificationAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return assignedCertification.name();
    }
}
