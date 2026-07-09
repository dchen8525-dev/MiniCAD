package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal APPROVAL_STATUS metadata.
 *
 * @param id STEP instance id
 * @param name status label
 */
/**
 * Minimal APPROVAL_STATUS metadata.
 *
 * @param id STEP instance id
 * @param name status label
 */
public final class StepApprovalStatus implements StepEntity {
    private final int id;
    private final String name;

    public StepApprovalStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Record-style accessor
    public String status() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepApprovalStatus that = (StepApprovalStatus) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepApprovalStatus{" + "id=" + id + "name=" + name + "}";
    }
}
