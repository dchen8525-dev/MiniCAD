package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal APPROVAL_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
/**
 * Minimal APPROVAL_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public final class StepApprovalRole implements StepEntity {
    private final int id;
    private final String name;

    public StepApprovalRole(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepApprovalRole that = (StepApprovalRole) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepApprovalRole{" + "id=" + id + "name=" + name + "}";
    }
}
