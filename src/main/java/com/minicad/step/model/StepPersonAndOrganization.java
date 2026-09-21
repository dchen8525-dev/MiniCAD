package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal PERSON_AND_ORGANIZATION metadata.
 *
 * @param id STEP instance id
 * @param person person
 * @param organization organization
 */
public final class StepPersonAndOrganization extends AbstractStepEntity {
    private final StepPerson person;
    private final StepOrganization organization;

    public StepPersonAndOrganization(int id, StepPerson person, StepOrganization organization) {
        super(id, "");
        this.person = person;
        this.organization = organization;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("person", person);
        state.put("organization", organization);
        return state;
    }
}
