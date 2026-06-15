package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal PERSON_AND_ORGANIZATION metadata.
 *
 * @param id STEP instance id
 * @param person person
 * @param organization organization
 */
/**
 * Minimal PERSON_AND_ORGANIZATION metadata.
 *
 * @param id STEP instance id
 * @param person person
 * @param organization organization
 */
public final class StepPersonAndOrganization implements StepEntity {
    private final int id;
    private final StepPerson person;
    private final StepOrganization organization;

    public StepPersonAndOrganization(int id, StepPerson person, StepOrganization organization) {
        this.id = id;
        this.person = person;
        this.organization = organization;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepPerson getPerson() {
        return person;
    }

    public StepOrganization getOrganization() {
        return organization;
    }

    // Record-style accessors
    public StepPerson person() {
        return person;
    }

    public StepOrganization organization() {
        return organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPersonAndOrganization that = (StepPersonAndOrganization) o;
        return id == that.id && Objects.equals(person, that.person) && Objects.equals(organization, that.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, organization);
    }

    @Override
    public String toString() {
        return "StepPersonAndOrganization{" + "id=" + id + "person=" + person + "organization=" + organization + "}";
    }
}
