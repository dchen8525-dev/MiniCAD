package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved USAGE_OCCURRENCE.
 * A usage occurrence in assembly structure.
 */
/**
 * Resolved USAGE_OCCURRENCE.
 * A usage occurrence in assembly structure.
 */
public final class StepUsageOccurrence implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity parent;
    private final StepEntity child;

    public StepUsageOccurrence(int id, String name, StepEntity parent, StepEntity child) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.child = child;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getParent() {
        return parent;
    }

    public StepEntity getChild() {
        return child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepUsageOccurrence that = (StepUsageOccurrence) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(parent, that.parent) && Objects.equals(child, that.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parent, child);
    }

    @Override
    public String toString() {
        return "StepUsageOccurrence{" + "id=" + id + "name=" + name + "parent=" + parent + "child=" + child + "}";
    }
}
