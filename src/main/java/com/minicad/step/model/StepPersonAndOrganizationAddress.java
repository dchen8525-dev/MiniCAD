package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepPersonAndOrganizationAddress extends AbstractStepEntity {
    private final StepEntity personAndOrganization;
    private final StepEntity address;

    public StepPersonAndOrganizationAddress(int id, String name, StepEntity personAndOrganization, StepEntity address) {
        super(id, name);
        this.personAndOrganization = personAndOrganization;
        this.address = address;
    }

    public StepEntity getPersonAndOrganization() {
        return personAndOrganization;
    }

    public StepEntity getAddress() {
        return address;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("personAndOrganization", personAndOrganization);
        state.put("address", address);
        return state;
    }
}
