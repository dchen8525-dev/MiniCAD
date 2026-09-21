package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("person", person);
        state.put("address", address);
        return state;
    }
}
