package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal APPROVAL_STATUS metadata.
 *
 * @param id STEP instance id
 * @param name status label
 */
public final class StepApprovalStatus extends AbstractStepEntity {
    public StepApprovalStatus(int id, String name) {
        super(id, name);
    }

    // Record-style accessor
    public String status() {
        return getName();
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
