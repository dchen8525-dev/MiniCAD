package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONSTRAINT_INSTANCE.
 * A constraint instance entity.
 *
 * @param id STEP instance id
 * @param name constraint instance name
 * @param constraintDefinition constraint variance definition reference
 * @param constraintState constraint variance state
 * @param constraintValue constraint variance current value
 * @param constraintViolations constraint variance violation count
 * @param constraintStatus constraint variance status
 */
/**
 * Resolved CONSTRAINT_INSTANCE.
 * A constraint instance entity.
 *
 * @param id STEP instance id
 * @param name constraint instance name
 * @param constraintDefinition constraint variance definition reference
 * @param constraintState constraint variance state
 * @param constraintValue constraint variance current value
 * @param constraintViolations constraint variance violation count
 * @param constraintStatus constraint variance status
 */
public final class StepConstraintInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity constraintDefinition;
    private final String constraintState;
    private final String constraintValue;
    private final int constraintViolations;
    private final String constraintStatus;

    public StepConstraintInstance(int id, String name, StepEntity constraintDefinition, String constraintState, String constraintValue, int constraintViolations, String constraintStatus) {
        this.id = id;
        this.name = name;
        this.constraintDefinition = constraintDefinition;
        this.constraintState = constraintState;
        this.constraintValue = constraintValue;
        this.constraintViolations = constraintViolations;
        this.constraintStatus = constraintStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getConstraintDefinition() {
        return constraintDefinition;
    }

    public String getConstraintState() {
        return constraintState;
    }

    public String getConstraintValue() {
        return constraintValue;
    }

    public int getConstraintViolations() {
        return constraintViolations;
    }

    public String getConstraintStatus() {
        return constraintStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConstraintInstance that = (StepConstraintInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(constraintDefinition, that.constraintDefinition) && Objects.equals(constraintState, that.constraintState) && Objects.equals(constraintValue, that.constraintValue) && constraintViolations == that.constraintViolations && Objects.equals(constraintStatus, that.constraintStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, constraintDefinition, constraintState, constraintValue, constraintViolations, constraintStatus);
    }

    @Override
    public String toString() {
        return "StepConstraintInstance{" + "id=" + id + "name=" + name + "constraintDefinition=" + constraintDefinition + "constraintState=" + constraintState + "constraintValue=" + constraintValue + "constraintViolations=" + constraintViolations + "constraintStatus=" + constraintStatus + "}";
    }
}