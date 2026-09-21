package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepOptimizationCriteria extends AbstractStepEntity {
    private final String objectiveType;
    private final String objectiveVariable;
    private final List<String> constraints;
    private final List<Double> constraintValues;
    private final double targetValue;

    public StepOptimizationCriteria(int id, String name, String objectiveType, String objectiveVariable, List<String> constraints, List<Double> constraintValues, double targetValue) {
        super(id, name);
        this.objectiveType = objectiveType;
        this.objectiveVariable = objectiveVariable;
        this.constraints = constraints == null ? null : java.util.List.copyOf(constraints);
        this.constraintValues = constraintValues == null ? null : java.util.List.copyOf(constraintValues);
        this.targetValue = targetValue;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("objectiveType", objectiveType);
        state.put("objectiveVariable", objectiveVariable);
        state.put("constraints", constraints);
        state.put("constraintValues", constraintValues);
        state.put("targetValue", targetValue);
        return state;
    }
}
