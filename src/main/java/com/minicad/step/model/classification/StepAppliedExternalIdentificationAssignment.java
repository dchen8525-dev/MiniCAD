package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role identification role
 * @param source external source
 * @param items assigned target items
 */
public record StepAppliedExternalIdentificationAssignment(
        int id,
        String assignedId,
        StepIdentificationRole role,
        StepExternalSource source,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedExternalIdentificationAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return assignedId;
    }
}
