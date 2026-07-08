package com.minicad.step.model.organization.org.org;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal ORGANIZATION metadata.
 *
 * @param id STEP instance id
 * @param identifier organization identifier
 * @param name organization name
 * @param description organization description
 */
/**
 * Minimal ORGANIZATION metadata.
 *
 * @param id STEP instance id
 * @param identifier organization identifier
 * @param name organization name
 * @param description organization description
 */
public final class StepOrganization implements StepEntity {
    private final int id;
    private final String identifier;
    private final String name;
    private final String description;

    public StepOrganization(int id, String identifier, String name, String description) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrganization that = (StepOrganization) o;
        return id == that.id && Objects.equals(identifier, that.identifier) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, name, description);
    }

    @Override
    public String toString() {
        return "StepOrganization{" + "id=" + id + "identifier=" + identifier + "name=" + name + "description=" + description + "}";
    }
}
