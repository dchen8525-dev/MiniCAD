package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal global unit assigned context.
 *
 * @param id STEP instance id
 * @param units referenced unit entities
 */
/**
 * Minimal global unit assigned context.
 *
 * @param id STEP instance id
 * @param units referenced unit entities
 */
public final class StepGlobalUnitAssignedContext implements StepEntity {
    private final int id;
    private final List<StepEntity> units;

    public StepGlobalUnitAssignedContext(int id, List<StepEntity> units) {
        this.id = id;
        this.units = units == null ? null : java.util.List.copyOf(units);
    }

    public int getId() {
        return id;
    }

    public List<StepEntity> getUnits() {
        return units;
    }

    public String getName() {
        return "";
    }

    // Record-style accessor
    public List<StepEntity> units() {
        return units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGlobalUnitAssignedContext that = (StepGlobalUnitAssignedContext) o;
        return id == that.id && Objects.equals(units, that.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, units);
    }

    @Override
    public String toString() {
        return "StepGlobalUnitAssignedContext{" + "id=" + id + "units=" + units + "}";
    }
}
