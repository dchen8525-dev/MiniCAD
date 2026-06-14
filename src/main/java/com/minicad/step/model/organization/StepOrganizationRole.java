package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal ORGANIZATION_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
/**
 * Minimal ORGANIZATION_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public final class StepOrganizationRole implements StepEntity {
    private final int id;
    private final String name;

    public StepOrganizationRole(int id, String name) {
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
        StepOrganizationRole that = (StepOrganizationRole) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepOrganizationRole{" + "id=" + id + "name=" + name + "}";
    }
}
