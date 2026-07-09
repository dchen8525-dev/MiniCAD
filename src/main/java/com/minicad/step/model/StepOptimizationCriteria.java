package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved OPTIMIZATION_CRITERIA.
 * An optimization criteria entity.
 *
 * @param id STEP instance id
 * @param name criteria name
 * @param objectiveType objective type (minimize, maximize)
 * @param objectiveVariable variable to optimize (weight, stress, displacement)
 * @param constraints optimization constraints
 * @param constraintValues constraint limit values
 * @param targetValue target objective value
 */
/**
 * Resolved OPTIMIZATION_CRITERIA.
 * An optimization criteria entity.
 *
 * @param id STEP instance id
 * @param name criteria name
 * @param objectiveType objective type (minimize, maximize)
 * @param objectiveVariable variable to optimize (weight, stress, displacement)
 * @param constraints optimization constraints
 * @param constraintValues constraint limit values
 * @param targetValue target objective value
 */
public final class StepOptimizationCriteria implements StepEntity {
    private final int id;
    private final String name;
    private final String objectiveType;
    private final String objectiveVariable;
    private final List<String> constraints;
    private final List<Double> constraintValues;
    private final double targetValue;

    public StepOptimizationCriteria(int id, String name, String objectiveType, String objectiveVariable, List<String> constraints, List<Double> constraintValues, double targetValue) {
        this.id = id;
        this.name = name;
        this.objectiveType = objectiveType;
        this.objectiveVariable = objectiveVariable;
        this.constraints = constraints == null ? null : java.util.List.copyOf(constraints);
        this.constraintValues = constraintValues == null ? null : java.util.List.copyOf(constraintValues);
        this.targetValue = targetValue;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getObjectiveType() {
        return objectiveType;
    }

    public String getObjectiveVariable() {
        return objectiveVariable;
    }

    public List<String> getConstraints() {
        return constraints;
    }

    public List<Double> getConstraintValues() {
        return constraintValues;
    }

    public double getTargetValue() {
        return targetValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOptimizationCriteria that = (StepOptimizationCriteria) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(objectiveType, that.objectiveType) && Objects.equals(objectiveVariable, that.objectiveVariable) && Objects.equals(constraints, that.constraints) && Objects.equals(constraintValues, that.constraintValues) && targetValue == that.targetValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, objectiveType, objectiveVariable, constraints, constraintValues, targetValue);
    }

    @Override
    public String toString() {
        return "StepOptimizationCriteria{" + "id=" + id + "name=" + name + "objectiveType=" + objectiveType + "objectiveVariable=" + objectiveVariable + "constraints=" + constraints + "constraintValues=" + constraintValues + "targetValue=" + targetValue + "}";
    }
}