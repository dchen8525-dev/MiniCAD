package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TOLERANCE_SET.
 * A tolerance set entity containing multiple tolerances.
 *
 * @param id STEP instance id
 * @param name set name
 * @param tolerances list of geometric tolerances
 * @param toleranceContext tolerance context reference
 * @param appliedTo geometry the tolerances apply to
 */
/**
 * Resolved TOLERANCE_SET.
 * A tolerance set entity containing multiple tolerances.
 *
 * @param id STEP instance id
 * @param name set name
 * @param tolerances list of geometric tolerances
 * @param toleranceContext tolerance context reference
 * @param appliedTo geometry the tolerances apply to
 */
public final class StepToleranceSet implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> tolerances;
    private final StepEntity toleranceContext;
    private final StepEntity appliedTo;

    public StepToleranceSet(int id, String name, List<StepEntity> tolerances, StepEntity toleranceContext, StepEntity appliedTo) {
        this.id = id;
        this.name = name;
        this.tolerances = tolerances == null ? null : java.util.List.copyOf(tolerances);
        this.toleranceContext = toleranceContext;
        this.appliedTo = appliedTo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getTolerances() {
        return tolerances;
    }

    public StepEntity getToleranceContext() {
        return toleranceContext;
    }

    public StepEntity getAppliedTo() {
        return appliedTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToleranceSet that = (StepToleranceSet) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(tolerances, that.tolerances) && Objects.equals(toleranceContext, that.toleranceContext) && Objects.equals(appliedTo, that.appliedTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tolerances, toleranceContext, appliedTo);
    }

    @Override
    public String toString() {
        return "StepToleranceSet{" + "id=" + id + "name=" + name + "tolerances=" + tolerances + "toleranceContext=" + toleranceContext + "appliedTo=" + appliedTo + "}";
    }
}