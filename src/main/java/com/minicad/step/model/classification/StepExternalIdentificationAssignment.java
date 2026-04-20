package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal EXTERNAL_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role identification role
 * @param source external source
 */
public record StepExternalIdentificationAssignment(
        int id,
        String assignedId,
        StepIdentificationRole role,
        StepExternalSource source
) implements StepEntity {

    @Override
    public String name() {
        return assignedId;
    }
}
