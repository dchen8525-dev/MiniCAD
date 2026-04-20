package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal APPLIED_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role assignment role
 * @param items assigned target items
 */
public record StepAppliedIdentificationAssignment(
        int id,
        String assignedId,
        StepIdentificationRole role,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedIdentificationAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return assignedId;
    }
}
