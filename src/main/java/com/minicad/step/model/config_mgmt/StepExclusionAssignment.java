package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved EXCLUSION_ASSIGNMENT.
 */
public record StepExclusionAssignment(
    int id,
    String name,
    List<StepEntity> assignedItems,
    StepEntity role
) implements StepEntity {

    public StepExclusionAssignment {
        assignedItems = List.copyOf(assignedItems);
    }
}
