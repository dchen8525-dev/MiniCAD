package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal APPLIED_GROUP_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedGroup assigned group
 * @param items assigned target items
 */
public record StepAppliedGroupAssignment(
        int id,
        StepGroup assignedGroup,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedGroupAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return assignedGroup.name();
    }
}
