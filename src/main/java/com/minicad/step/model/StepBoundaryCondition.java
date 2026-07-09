package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BOUNDARY_CONDITION.
 * A boundary condition entity.
 *
 * @param id STEP instance id
 * @param name boundary condition name
 * @param conditionType condition variance type
 * @param conditionLocation condition variance location reference
 * @param conditionConstraints condition variance constraints
 * @param conditionStatus condition variance status
 */
/**
 * Resolved BOUNDARY_CONDITION.
 * A boundary condition entity.
 *
 * @param id STEP instance id
 * @param name boundary condition name
 * @param conditionType condition variance type
 * @param conditionLocation condition variance location reference
 * @param conditionConstraints condition variance constraints
 * @param conditionStatus condition variance status
 */
public final class StepBoundaryCondition implements StepEntity {
    private final int id;
    private final String name;
    private final String conditionType;
    private final StepEntity conditionLocation;
    private final List<String> conditionConstraints;
    private final String conditionStatus;

    public StepBoundaryCondition(int id, String name, String conditionType, StepEntity conditionLocation, List<String> conditionConstraints, String conditionStatus) {
        this.id = id;
        this.name = name;
        this.conditionType = conditionType;
        this.conditionLocation = conditionLocation;
        this.conditionConstraints = conditionConstraints == null ? null : java.util.List.copyOf(conditionConstraints);
        this.conditionStatus = conditionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConditionType() {
        return conditionType;
    }

    public StepEntity getConditionLocation() {
        return conditionLocation;
    }

    public List<String> getConditionConstraints() {
        return conditionConstraints;
    }

    public String getConditionStatus() {
        return conditionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBoundaryCondition that = (StepBoundaryCondition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(conditionType, that.conditionType) && Objects.equals(conditionLocation, that.conditionLocation) && Objects.equals(conditionConstraints, that.conditionConstraints) && Objects.equals(conditionStatus, that.conditionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, conditionType, conditionLocation, conditionConstraints, conditionStatus);
    }

    @Override
    public String toString() {
        return "StepBoundaryCondition{" + "id=" + id + "name=" + name + "conditionType=" + conditionType + "conditionLocation=" + conditionLocation + "conditionConstraints=" + conditionConstraints + "conditionStatus=" + conditionStatus + "}";
    }
}