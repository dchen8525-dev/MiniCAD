package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PERSON_AND_ORGANIZATION metadata.
 *
 * @param id STEP instance id
 * @param person person
 * @param organization organization
 */
public record StepPersonAndOrganization(
        int id,
        StepPerson person,
        StepOrganization organization
) implements StepEntity {

    @Override
    public String name() {
        return person.name() + " / " + organization.name();
    }
}
