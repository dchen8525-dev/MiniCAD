package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepOrganizationAddress extends AbstractStepEntity {
    private final StepEntity organization;
    private final StepEntity address;

    public StepOrganizationAddress(int id, String name, StepEntity organization, StepEntity address) {
        super(id, name);
        this.organization = organization;
        this.address = address;
    }

    public StepEntity getOrganization() {
        return organization;
    }

    public StepEntity getAddress() {
        return address;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("organization", organization);
        state.put("address", address);
        return state;
    }
}
