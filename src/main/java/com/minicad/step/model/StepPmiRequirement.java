package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved PMI_REQUIREMENT.
 */
/**
 * Resolved PMI_REQUIREMENT.
 */
public final class StepPmiRequirement implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final String requirementType;

    public StepPmiRequirement(int id, String name, String description, String requirementType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.requirementType = requirementType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirementType() {
        return requirementType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPmiRequirement that = (StepPmiRequirement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(requirementType, that.requirementType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, requirementType);
    }

    @Override
    public String toString() {
        return "StepPmiRequirement{" + "id=" + id + "name=" + name + "description=" + description + "requirementType=" + requirementType + "}";
    }
}
