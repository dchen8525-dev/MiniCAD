package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved USAGE_ASSOCIATION.
 */
/**
 * Resolved USAGE_ASSOCIATION.
 */
public final class StepUsageAssociation implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity relatingUsage;
    private final StepEntity relatedUsage;

    public StepUsageAssociation(int id, String name, StepEntity relatingUsage, StepEntity relatedUsage) {
        this.id = id;
        this.name = name;
        this.relatingUsage = relatingUsage;
        this.relatedUsage = relatedUsage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getRelatingUsage() {
        return relatingUsage;
    }

    public StepEntity getRelatedUsage() {
        return relatedUsage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepUsageAssociation that = (StepUsageAssociation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(relatingUsage, that.relatingUsage) && Objects.equals(relatedUsage, that.relatedUsage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, relatingUsage, relatedUsage);
    }

    @Override
    public String toString() {
        return "StepUsageAssociation{" + "id=" + id + "name=" + name + "relatingUsage=" + relatingUsage + "relatedUsage=" + relatedUsage + "}";
    }
}
