package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONSTRAINT_SPECIFICATION.
 * A constraint specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param constraintType constraint type (geometric, functional, assembly)
 * @varianceSubject constraint variance subject
 * @varianceValue constraint variance value
 * @varianceTolerance tolerance variance if applicable
 * @varianceUnit constraint variance unit
 * @varianceStatus specification variance status
 */
/**
 * Resolved CONSTRAINT_SPECIFICATION.
 * A constraint specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param constraintType constraint type (geometric, functional, assembly)
 * @varianceSubject constraint variance subject
 * @varianceValue constraint variance value
 * @varianceTolerance tolerance variance if applicable
 * @varianceUnit constraint variance unit
 * @varianceStatus specification variance status
 */
public final class StepConstraintSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final String constraintType;
    private final StepEntity varianceSubject;
    private final double varianceValue;
    private final double varianceTolerance;
    private final StepEntity varianceUnit;
    private final String varianceStatus;

    public StepConstraintSpecification(int id, String name, String constraintType, StepEntity varianceSubject, double varianceValue, double varianceTolerance, StepEntity varianceUnit, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.constraintType = constraintType;
        this.varianceSubject = varianceSubject;
        this.varianceValue = varianceValue;
        this.varianceTolerance = varianceTolerance;
        this.varianceUnit = varianceUnit;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public StepEntity getVarianceSubject() {
        return varianceSubject;
    }

    public double getVarianceValue() {
        return varianceValue;
    }

    public double getVarianceTolerance() {
        return varianceTolerance;
    }

    public StepEntity getVarianceUnit() {
        return varianceUnit;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConstraintSpecification that = (StepConstraintSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(constraintType, that.constraintType) && Objects.equals(varianceSubject, that.varianceSubject) && varianceValue == that.varianceValue && varianceTolerance == that.varianceTolerance && Objects.equals(varianceUnit, that.varianceUnit) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, constraintType, varianceSubject, varianceValue, varianceTolerance, varianceUnit, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepConstraintSpecification{" + "id=" + id + "name=" + name + "constraintType=" + constraintType + "varianceSubject=" + varianceSubject + "varianceValue=" + varianceValue + "varianceTolerance=" + varianceTolerance + "varianceUnit=" + varianceUnit + "varianceStatus=" + varianceStatus + "}";
    }
}