package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * NON_AGREED_UNIT_USAGE entity model.
 * Represents usage of a unit that has not been agreed upon in a standards context.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unit reference to the unit being used
 */
public final class StepNonAgreedUnitUsage implements StepEntity {
    private final int id;
    private final String name;
    private final Object unit; // Can be any unit type

    public StepNonAgreedUnitUsage(int id, String name, Object unit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Object getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNonAgreedUnitUsage that = (StepNonAgreedUnitUsage) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepNonAgreedUnitUsage{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", unit=" + unit +
            '}';
    }
}