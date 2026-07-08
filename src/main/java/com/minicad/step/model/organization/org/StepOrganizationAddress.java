package com.minicad.step.model.organization.org.org;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepOrganizationAddress implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity organization;
    private final StepEntity address;

    public StepOrganizationAddress(int id, String name, StepEntity organization, StepEntity address) {
        this.id = id;
        this.name = name;
        this.organization = organization;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getOrganization() {
        return organization;
    }

    public StepEntity getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrganizationAddress that = (StepOrganizationAddress) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(organization, that.organization) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, organization, address);
    }

    @Override
    public String toString() {
        return "StepOrganizationAddress{" + "id=" + id + "name=" + name + "organization=" + organization + "address=" + address + "}";
    }
}