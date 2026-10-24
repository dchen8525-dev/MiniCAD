package com.minicad.step.model;

import java.util.Map;

public final class StepPersonAddress extends AbstractStepEntity {
    private final StepEntity person;
    private final StepEntity address;

    public StepPersonAddress(int id, String name, StepEntity person, StepEntity address) {
        super(id, name);
        this.person = person;
        this.address = address;
    }

    public StepEntity getPerson() {
        return person;
    }

    public StepEntity getAddress() {
        return address;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "person", person, "address", address);
    }
}
