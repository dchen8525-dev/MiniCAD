package com.minicad.step.model.organization.org.org;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal ORGANIZATION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingOrganization source organization
 * @param relatedOrganization target organization
 */
/**
 * Minimal ORGANIZATION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingOrganization source organization
 * @param relatedOrganization target organization
 */
public final class StepOrganizationRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepOrganization relatingOrganization;
    private final StepOrganization relatedOrganization;

    public StepOrganizationRelationship(int id, String name, String description, StepOrganization relatingOrganization, StepOrganization relatedOrganization) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingOrganization = relatingOrganization;
        this.relatedOrganization = relatedOrganization;
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

    public StepOrganization getRelatingOrganization() {
        return relatingOrganization;
    }

    public StepOrganization getRelatedOrganization() {
        return relatedOrganization;
    }

    // Record-style accessors
    public StepOrganization relatingOrganization() {
        return relatingOrganization;
    }

    public StepOrganization relatedOrganization() {
        return relatedOrganization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrganizationRelationship that = (StepOrganizationRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingOrganization, that.relatingOrganization) && Objects.equals(relatedOrganization, that.relatedOrganization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingOrganization, relatedOrganization);
    }

    @Override
    public String toString() {
        return "StepOrganizationRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingOrganization=" + relatingOrganization + "relatedOrganization=" + relatedOrganization + "}";
    }
}
