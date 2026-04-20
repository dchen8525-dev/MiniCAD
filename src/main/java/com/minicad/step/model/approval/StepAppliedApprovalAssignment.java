package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal APPLIED_APPROVAL_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedApproval assigned approval
 * @param items assigned target items
 */
public record StepAppliedApprovalAssignment(
        int id,
        String entityName,
        StepApproval assignedApproval,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedApprovalAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return assignedApproval.name();
    }
}
