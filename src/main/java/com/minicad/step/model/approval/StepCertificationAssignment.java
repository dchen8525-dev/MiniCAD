package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal CERTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedCertification assigned certification
 */
public record StepCertificationAssignment(
        int id,
        StepCertification assignedCertification
) implements StepEntity {

    @Override
    public String name() {
        return assignedCertification.name();
    }
}
