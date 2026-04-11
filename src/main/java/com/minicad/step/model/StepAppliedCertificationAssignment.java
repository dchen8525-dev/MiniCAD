package com.minicad.step.model;

import java.util.List;

/**
 * Minimal APPLIED_CERTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedCertification assigned certification
 * @param items assigned target items
 */
public record StepAppliedCertificationAssignment(
        int id,
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
