package com.minicad.step.model.organization.org.org;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepPersonAndOrganizationAddress implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity personAndOrganization;
    private final StepEntity address;

    public StepPersonAndOrganizationAddress(int id, String name, StepEntity personAndOrganization, StepEntity address) {
        this.id = id;
        this.name = name;
        this.personAndOrganization = personAndOrganization;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPersonAndOrganization() {
        return personAndOrganization;
    }

    public StepEntity getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPersonAndOrganizationAddress that = (StepPersonAndOrganizationAddress) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(personAndOrganization, that.personAndOrganization) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, personAndOrganization, address);
    }

    @Override
    public String toString() {
        return "StepPersonAndOrganizationAddress{" + "id=" + id + "name=" + name + "personAndOrganization=" + personAndOrganization + "address=" + address + "}";
    }
}