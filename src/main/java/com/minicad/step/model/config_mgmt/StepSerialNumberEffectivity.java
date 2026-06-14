package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved SERIAL_NUMBER_EFFECTIVITY.
 */
/**
 * Resolved SERIAL_NUMBER_EFFECTIVITY.
 */
public final class StepSerialNumberEffectivity implements StepEntity {
    private final int id;
    private final String name;
    private final String serialNumber;

    public StepSerialNumberEffectivity(int id, String name, String serialNumber) {
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSerialNumberEffectivity that = (StepSerialNumberEffectivity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(serialNumber, that.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, serialNumber);
    }

    @Override
    public String toString() {
        return "StepSerialNumberEffectivity{" + "id=" + id + "name=" + name + "serialNumber=" + serialNumber + "}";
    }
}
