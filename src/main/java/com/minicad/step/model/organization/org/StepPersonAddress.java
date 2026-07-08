package com.minicad.step.model.organization.org.org;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepPersonAddress implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity person;
    private final StepEntity address;

    public StepPersonAddress(int id, String name, StepEntity person, StepEntity address) {
        this.id = id;
        this.name = name;
        this.person = person;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPerson() {
        return person;
    }

    public StepEntity getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPersonAddress that = (StepPersonAddress) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(person, that.person) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, person, address);
    }

    @Override
    public String toString() {
        return "StepPersonAddress{" + "id=" + id + "name=" + name + "person=" + person + "address=" + address + "}";
    }
}