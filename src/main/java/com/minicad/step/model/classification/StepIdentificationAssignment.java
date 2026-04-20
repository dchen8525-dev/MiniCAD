package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role assignment role
 */
public record StepIdentificationAssignment(
        int id,
        String assignedId,
        StepIdentificationRole role
) implements StepEntity {

    @Override
    public String name() {
        return assignedId;
    }
}
