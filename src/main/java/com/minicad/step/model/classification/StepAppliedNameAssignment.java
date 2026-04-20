package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal APPLIED_NAME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedName assigned name
 * @param items assigned target items
 */
public record StepAppliedNameAssignment(
        int id,
        String assignedName,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedNameAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return assignedName;
    }
}
