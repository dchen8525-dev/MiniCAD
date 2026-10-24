package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal APPROVAL metadata.
 *
 * @param id STEP instance id
 * @param status approval status
 * @param level approval level
 */
public final class StepApproval extends AbstractStepEntity {
    private final StepApprovalStatus status;
    private final String level;

    public StepApproval(int id, StepApprovalStatus status, String level) {
        super(id, "");
        this.status = status;
        this.level = level;
    }

    public StepApprovalStatus getStatus() {
        return status;
    }

    public String getLevel() {
        return level;
    }

    public String getName() {
        return level != null ? level : "";
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public StepApprovalStatus status() {
        return status;
    }

    public String level() {
        return level;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "status", status, "level", level);
    }
}
