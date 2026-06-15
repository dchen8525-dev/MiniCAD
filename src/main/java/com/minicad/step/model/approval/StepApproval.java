package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal APPROVAL metadata.
 *
 * @param id STEP instance id
 * @param status approval status
 * @param level approval level
 */
/**
 * Minimal APPROVAL metadata.
 *
 * @param id STEP instance id
 * @param status approval status
 * @param level approval level
 */
public final class StepApproval implements StepEntity {
    private final int id;
    private final StepApprovalStatus status;
    private final String level;

    public StepApproval(int id, StepApprovalStatus status, String level) {
        this.id = id;
        this.status = status;
        this.level = level;
    }

    public int getId() {
        return id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepApproval that = (StepApproval) o;
        return id == that.id && Objects.equals(status, that.status) && Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, level);
    }

    @Override
    public String toString() {
        return "StepApproval{" + "id=" + id + "status=" + status + "level=" + level + "}";
    }
}
