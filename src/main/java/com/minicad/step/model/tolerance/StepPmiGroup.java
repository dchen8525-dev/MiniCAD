package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PMI_GROUP.
 */
/**
 * Resolved PMI_GROUP.
 */
public final class StepPmiGroup implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> members;

    public StepPmiGroup(int id, String name, List<StepEntity> members) {
        this.id = id;
        this.name = name;
        this.members = members == null ? null : java.util.List.copyOf(members);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getMembers() {
        return members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPmiGroup that = (StepPmiGroup) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(members, that.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, members);
    }

    @Override
    public String toString() {
        return "StepPmiGroup{" + "id=" + id + "name=" + name + "members=" + members + "}";
    }
}
