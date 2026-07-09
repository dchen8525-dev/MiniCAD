package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;

import com.minicad.step.model.technical.unit.StepUncertaintyMeasureWithUnit;
import java.util.Objects;

/**
 * Minimal global uncertainty assigned context.
 *
 * @param id STEP instance id
 * @param uncertainties referenced uncertainty entities
 */
/**
 * Minimal global uncertainty assigned context.
 *
 * @param id STEP instance id
 * @param uncertainties referenced uncertainty entities
 */
public final class StepGlobalUncertaintyAssignedContext implements StepEntity {
    private final int id;
    private final List<StepUncertaintyMeasureWithUnit> uncertainties;

    public StepGlobalUncertaintyAssignedContext(int id, List<StepUncertaintyMeasureWithUnit> uncertainties) {
        this.id = id;
        this.uncertainties = uncertainties == null ? null : java.util.List.copyOf(uncertainties);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public List<StepUncertaintyMeasureWithUnit> getUncertainties() {
        return uncertainties;
    }

    // Record-style accessors
    public List<StepUncertaintyMeasureWithUnit> uncertainties() { return getUncertainties(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGlobalUncertaintyAssignedContext that = (StepGlobalUncertaintyAssignedContext) o;
        return id == that.id && Objects.equals(uncertainties, that.uncertainties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uncertainties);
    }

    @Override
    public String toString() {
        return "StepGlobalUncertaintyAssignedContext{" + "id=" + id + "uncertainties=" + uncertainties + "}";
    }
}
